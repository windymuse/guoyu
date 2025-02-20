package com.windymuse.unimall.data.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.windymuse.unimall.data.domain.OrderDO;
import com.windymuse.unimall.data.dto.order.OrderDTO;
import com.windymuse.unimall.data.dto.order.OrderGoodsSumDTO;
import com.windymuse.unimall.data.model.KVModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rize on 2019/7/6.
 */
public interface OrderMapper extends BaseMapper<OrderDO> {

    public List<OrderDTO> selectOrderPage(@Param("status") Integer status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    public Long countOrder(@Param("status") Integer status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    /**
     * 获取订单地区统计
     * @return
     */
    public List<KVModel<String, Long>> selectAreaStatistics();

    public List<KVModel<String, Long>> selectChannelStatistics();

    public List<KVModel<String, Long>> selectOrderCountStatistics(String gmtStart);

    public List<KVModel<String, Long>> selectOrderSumStatistics(String gmtStart);

    public List<String> selectExpireOrderNos(@Param("status") Integer status, @Param("time") Date time);

    public List<OrderGoodsSumDTO> selectOrderGoodsSum(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
