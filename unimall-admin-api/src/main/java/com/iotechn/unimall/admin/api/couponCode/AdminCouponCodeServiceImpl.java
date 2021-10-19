package com.iotechn.unimall.admin.api.couponCode;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iotechn.unimall.admin.api.couponCode.AdminCouponCodeService;
import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.CouponCodeDO;
import com.iotechn.unimall.data.mapper.CouponCodeMapper;
import com.iotechn.unimall.data.model.Page;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-12
 * Time: 下午11:26
 */
@Service
public class AdminCouponCodeServiceImpl implements AdminCouponCodeService {

    @Autowired
    private CouponCodeMapper couponCodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponCodeDO addCouponCode(Long adminId, Long couponId, String code, Integer status, String description, Long gmtStart, Long gmtEnd) throws ServiceException {

        Date start = null;
        Date end = null;
        if (gmtEnd != null && gmtStart != null) {
            start = new Date(gmtStart);
            end = new Date(gmtEnd);
        }

        CouponCodeDO couponCodeDO = new CouponCodeDO(couponId, null, code, status, description, start, end);

        Date now = new Date();
        couponCodeDO.setGmtCreate(now);
        couponCodeDO.setGmtUpdate(now);
        if (couponCodeMapper.insert(couponCodeDO) > 0) {
            return couponCodeDO;
        }
        throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCouponCode(Long adminId, Long id) throws ServiceException {
        EntityWrapper wrapperC = new EntityWrapper();
        wrapperC.eq("id", id);
        if (couponCodeMapper.delete(wrapperC) <= 0) {
            throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
        }
        return true;
    }


    @Override
    public Boolean updateCouponCode(Long adminId, Long id, Long couponId, Long userId, String code, Integer status, String description, Date gmtStart, Date gmtEnd) throws ServiceException {
        CouponCodeDO couponCodeDO = new CouponCodeDO(couponId, null, code, status, description, gmtStart, gmtEnd);
        couponCodeDO.setId(id);
        List<CouponCodeDO> couponCodeDOList = couponCodeMapper.selectList(new EntityWrapper<CouponCodeDO>().eq("id", id));
        if (CollectionUtils.isEmpty(couponCodeDOList)) {
            throw new AdminServiceException(ExceptionDefinition.COUPON_NOT_EXIST);
        }
        Date now = new Date();
        couponCodeDO.setGmtCreate(couponCodeDOList.get(0).getGmtCreate());
        couponCodeDO.setGmtUpdate(now);
        return couponCodeMapper.updateAllColumnById(couponCodeDO) > 0;
    }

    @Override
    public Page<CouponCodeDO> queryCouponCode(Long adminId, Long couponId, Long userId, Integer status, Integer pageNo, Integer limit) throws ServiceException {
        EntityWrapper wrapper = new EntityWrapper();
        Date now = new Date();
        if (couponId != null) {
            wrapper.eq("couponId", couponId);
        }
        if (userId != null) {
            wrapper.eq("userId", userId);
        }
        if (status != null) {
            if (status >= 0 && status < 2) {
                wrapper.eq("status", status);
            } else if (status < 0) {
                wrapper.lt("gmt_end", now);
            } else {
                throw new AdminServiceException(ExceptionDefinition.COUPON_CHECK_DATA_FAILED);
            }
        }
        Integer count = couponCodeMapper.selectCount(wrapper);
        List<CouponCodeDO> couponCodeDTOList = couponCodeMapper.getCouponCodeList(couponId, userId, status, (pageNo - 1) * limit, limit);
        Page<CouponCodeDO> page = new Page<CouponCodeDO>(couponCodeDTOList, pageNo, limit, count);
        return page;
    }
}
