package com.windymuse.unimall.admin.api.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.windymuse.unimall.biz.service.order.OrderBizService;
import com.windymuse.unimall.core.exception.AdminServiceException;
import com.windymuse.unimall.core.exception.ExceptionDefinition;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.component.LockComponent;
import com.windymuse.unimall.data.domain.MemberLevelDO;
import com.windymuse.unimall.data.domain.OrderDO;
import com.windymuse.unimall.data.domain.OrderSkuDO;
import com.windymuse.unimall.data.domain.UserDO;
import com.windymuse.unimall.data.dto.order.OrderDTO;
import com.windymuse.unimall.data.enums.OrderStatusType;
import com.windymuse.unimall.data.enums.UserLoginType;
import com.windymuse.unimall.data.mapper.MemberLevelMapper;
import com.windymuse.unimall.data.mapper.OrderMapper;
import com.windymuse.unimall.data.mapper.OrderSkuMapper;
import com.windymuse.unimall.data.mapper.UserMapper;
import com.windymuse.unimall.data.model.Page;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rize on 2019/7/10.
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private static final Logger logger = LoggerFactory.getLogger(AdminOrderServiceImpl.class);

    @Autowired
    private OrderBizService orderBizService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderSkuMapper orderSkuMapper;

    @Autowired
    private LockComponent lockComponent;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberLevelMapper memberLevelMapper;

    @Value("${com.windymuse.unimall.wx.mini.app-id}")
    private String wxMiNiAppid;

    @Value("${com.windymuse.unimall.wx.app.app-id}")
    private String wxAppAppid;

    @Override
    public Page<OrderDO> list(Integer pageNo, Integer pageSize, Integer status, String orderNo, Long adminId) throws ServiceException {
        Wrapper<OrderDO> wrapper = new EntityWrapper<OrderDO>();
        wrapper.orderBy("id", false);
        if (!StringUtils.isEmpty(orderNo)) {
            wrapper.eq("order_no", orderNo);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        List<OrderDO> orderDOS = orderMapper.selectPage(new RowBounds((pageNo - 1) * pageSize, pageSize), wrapper);
        Integer count = orderMapper.selectCount(wrapper);
        return new Page<>(orderDOS, pageNo, pageSize, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String refund(String orderNo, Integer type, Long adminId) throws ServiceException {
        if (lockComponent.tryLock(OrderBizService.ORDER_REFUND_LOCK + orderNo, 30)) {
            try {
                //1.校验订单状态是否处于退款中
                OrderDO orderDO = orderBizService.checkOrderExist(orderNo, null);
                if (orderDO.getStatus() != OrderStatusType.REFUNDING.getCode()) {
                    throw new AdminServiceException(ExceptionDefinition.ORDER_STATUS_NOT_SUPPORT_REFUND);
                }
                //2.退款处理
                if (type == 0) {
                    //2.1 店主拒绝退款
                    OrderDO updateOrderDO = new OrderDO();
                    updateOrderDO.setStatus(OrderStatusType.WAIT_CONFIRM.getCode());
                    updateOrderDO.setGmtUpdate(new Date());
                    orderBizService.changeOrderStatus(orderNo, OrderStatusType.REFUNDING.getCode(), updateOrderDO);
                    return "ok";
                } else if (type == 1) {
                    //2.2 店主同意退款
                    //2.2.1 先流转状态
                    OrderDO updateOrderDO = new OrderDO();
                    updateOrderDO.setStatus(OrderStatusType.REFUNDED.getCode());
                    updateOrderDO.setGmtUpdate(new Date());
                    orderBizService.changeOrderStatus(orderNo, OrderStatusType.REFUNDING.getCode(), updateOrderDO);
                    Long userId = orderDO.getUserId();
                    UserDO userDO = userMapper.selectById(userId);
                    Integer loginType = userDO.getLoginType();
                    //2.2.2 向微信支付平台发送退款请求
                    WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
                    wxPayRefundRequest.setAppid(loginType == UserLoginType.MP_WEIXIN.getCode() ? wxMiNiAppid : wxAppAppid);
                    wxPayRefundRequest.setOutTradeNo(orderNo);
                    wxPayRefundRequest.setOutRefundNo("refund_" + orderNo);
                    wxPayRefundRequest.setTotalFee(orderDO.getPayPrice() - orderDO.getFreightPrice());
                    wxPayRefundRequest.setRefundFee(orderDO.getPayPrice() - orderDO.getFreightPrice());
                    WxPayRefundResult wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
                    if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
                        logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                        throw new AdminServiceException(wxPayRefundResult.getReturnMsg(),
                                ExceptionDefinition.THIRD_PART_SERVICE_EXCEPTION.getCode());
                    }
                    if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
                        logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                        throw new AdminServiceException(wxPayRefundResult.getReturnMsg(),
                                ExceptionDefinition.THIRD_PART_SERVICE_EXCEPTION.getCode());
                    }
                    // 扣除相应积分
                    // 更新用户的积分和会员等级
                    userDO.setPoints(userDO.getPoints() - (orderDO.getPayPrice() - orderDO.getFreightPrice()));
                    // 获取当前级别的会员等级信息
                    List<MemberLevelDO> currMemberLevelList = memberLevelMapper.getMemberLevelList(null, null, userDO.getLevel(), null, null, 0, 100);
                    if (currMemberLevelList.size() > 0) {
                        MemberLevelDO memberLevelDO = currMemberLevelList.get(0);
                        System.out.println();
                        System.out.println("curr level");
                        System.out.println(memberLevelDO.getDegree());
                        System.out.println(memberLevelDO.getMoney());
                        System.out.println();
                        System.out.println();
                        // 低于升级门槛，降级
                        if (userDO.getPoints() < memberLevelDO.getMoney()) {
                            userDO.setLevel(memberLevelDO.getDegree() - 1);
                        }
                    }
                    userMapper.updateById(userDO);

                    return "ok";
                } else {
                    throw new AdminServiceException(ExceptionDefinition.PARAM_CHECK_FAILED);
                }
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                logger.error("[微信退款] 异常", e);
                throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
            } finally {
                lockComponent.release(OrderBizService.ORDER_REFUND_LOCK + orderNo);
            }
        } else {
            throw new AdminServiceException(ExceptionDefinition.SYSTEM_BUSY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String ship(String orderNo, String shipCode, String shipNo, Long adminId) throws ServiceException {
        System.out.println(shipCode);
        System.out.println(shipCode);
        System.out.println(shipCode);
        System.out.println(shipCode);
        orderBizService.checkOrderExist(orderNo, null);
        OrderDO updateOrderDO = new OrderDO();
        Date now = new Date();
        updateOrderDO.setGmtUpdate(now);
        updateOrderDO.setGmtShip(now);
        updateOrderDO.setStatus(OrderStatusType.WAIT_CONFIRM.getCode());
        if (!"NONE".equals(shipCode)) {
            updateOrderDO.setShipCode(shipCode);
            updateOrderDO.setShipNo(shipNo);
        } else {
            updateOrderDO.setShipCode(shipCode);
        }
        //流转订单状态
        orderBizService.changeOrderStatus(orderNo, OrderStatusType.WAIT_STOCK.getCode(), updateOrderDO);
        return "ok";
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String take(String orderNo, Long adminId) throws ServiceException {
        orderBizService.checkOrderExist(orderNo, null);
        OrderDO updateOrderDO = new OrderDO();
        Date now = new Date();
        updateOrderDO.setGmtUpdate(now);
        updateOrderDO.setGmtShip(now);
        updateOrderDO.setStatus(OrderStatusType.WAIT_APPRAISE.getCode());
        //流转订单状态
        orderBizService.changeOrderStatus(orderNo, OrderStatusType.WAIT_STOCK.getCode(), updateOrderDO);
        return "ok";
    }

    @Override
    public OrderDTO detail(Long orderId, Long adminId) throws ServiceException {
        return orderBizService.getOrderDetail(orderId, null);
    }

    @Override
    public List<OrderDTO> queryToExcel(Long gmtStart, Long gmtEnd, Integer status, Long adminId) throws ServiceException {
        EntityWrapper wrapper = new EntityWrapper();
        if(gmtStart != null && gmtEnd != null){
            if(gmtStart > gmtStart){
                throw new AdminServiceException(ExceptionDefinition.ORDER_EXCEL_PARAM_ERROR);
            }
            wrapper.between("gmt_create",new Date(gmtStart) ,new Date(gmtEnd));
        }
        if(status != null){
            wrapper.eq("status", status);
        }
        List<OrderDO> orderDOS = orderMapper.selectList(wrapper);
        if(orderDOS == null || orderDOS.size() == 0){
            return null;
        }
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (OrderDO temp: orderDOS ) {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(temp, orderDTO);
            orderDTOList.add(orderDTO);
        }
        for (OrderDTO orderDTO:orderDTOList) {
                List<OrderSkuDO> orderSkuDOList =  orderSkuMapper.selectList(new EntityWrapper<OrderSkuDO>().eq("order_no",orderDTO.getOrderNo() ));
                orderDTO.setSkuList(orderSkuDOList);
        }
        return orderDTOList;
    }
}
