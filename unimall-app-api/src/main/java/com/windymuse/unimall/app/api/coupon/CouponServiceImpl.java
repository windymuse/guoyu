package com.windymuse.unimall.app.api.coupon;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.windymuse.unimall.core.exception.ExceptionDefinition;
import com.windymuse.unimall.core.exception.AppServiceException;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.component.LockComponent;
import com.windymuse.unimall.data.domain.CouponCodeDO;
import com.windymuse.unimall.data.domain.CouponDO;
import com.windymuse.unimall.data.domain.UserCouponDO;
import com.windymuse.unimall.data.dto.CouponDTO;
import com.windymuse.unimall.data.dto.UserCouponDTO;
import com.windymuse.unimall.data.enums.StatusType;
import com.windymuse.unimall.data.mapper.CouponCodeMapper;
import com.windymuse.unimall.data.mapper.CouponMapper;
import com.windymuse.unimall.data.mapper.UserCouponMapper;
import com.windymuse.unimall.data.model.KVModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rize on 2019/7/4.
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private LockComponent lockComponent;

    @Autowired
    private CouponCodeMapper couponCodeMapper;

    private static final String COUPON_LOCK = "COUPON_LOCK_";

    private static final String COUPON_USER_LOCK = "COUPON_USER_LOCK_";

    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String obtainCoupon(Long couponId, String code, Long userId) throws ServiceException {
        //防止用户一瞬间提交两次表单，导致超领
        if (lockComponent.tryLock(COUPON_USER_LOCK + userId + "_" + couponId, 10)) {
            try {
                CouponDO couponDO = couponMapper.selectById(couponId);
                if (couponDO.getStatus() == StatusType.LOCK.getCode()) {
                    throw new AppServiceException(ExceptionDefinition.COUPON_HAS_LOCKED);
                }
                // 需要使用兑换码领取的优惠券
                if (couponDO.getGmtType() == 2 && code == null) {
                    throw new AppServiceException(ExceptionDefinition.COUPON_CHECK_CODE_FAILED);
                }
                Date now = new Date();
                if (couponDO.getGmtEnd() != null && couponDO.getGmtEnd().getTime() < now.getTime()) {
                    throw new AppServiceException(ExceptionDefinition.COUPON_ACTIVITY_HAS_END);
                }
                if (couponDO.getGmtStart() != null && couponDO.getGmtStart().getTime() > now.getTime()) {
                    throw new AppServiceException(ExceptionDefinition.COUPON_ACTIVITY_NOT_START);
                }
                if (couponDO.getTotal() != -1 && couponDO.getSurplus() <= 0) {
                    throw new AppServiceException(ExceptionDefinition.COUPON_ISSUE_OVER);
                } else {
                    if (couponDO.getTotal() >= 0) {
                        if (couponDO.getSurplus() == 1) {
                            if (!lockComponent.tryLock(COUPON_LOCK + couponId, 10)) {
                                throw new AppServiceException(ExceptionDefinition.COUPON_ISSUE_OVER);
                            }
                        }
                        couponMapper.decCoupon(couponId);
                    }
                }


                if (couponDO.getLimit() != -1) {
                    //校验用户是否已经领了
                    Integer count = userCouponMapper.selectCount(
                            new EntityWrapper<UserCouponDO>()
                                    .eq("user_id", userId)
                                    .eq("coupon_id", couponId));

                    if (count >= couponDO.getLimit()) {
                        throw new AppServiceException(ExceptionDefinition.COUPON_YOU_HAVE_OBTAINED);
                    }
                }

                // 有兑换码时，检查兑换码有效性
                if (null != code) {
                    CouponCodeDO couponCodeDO = couponCodeMapper.selectCouponCodeByCode(code);

                    if (couponCodeDO == null) {
                        // 兑换码不存在
                        throw new AppServiceException(ExceptionDefinition.COUPON_CODE_NOT_FOUND);
                    }
                    if (!couponCodeDO.getCouponId().equals(couponId)) {
                        // 兑换码不属于该优惠券
                        throw new AppServiceException(ExceptionDefinition.COUPON_CODE_ERROR);
                    }
                    if (couponCodeDO.getStatus() == 2) {
                        // 兑换码已被使用
                        throw new AppServiceException(ExceptionDefinition.COUPON_CODE_USED);
                    }
                    // 更新兑换码状态
                    couponCodeDO.setStatus(2);
                    couponCodeDO.setUserId(userId);
                    couponCodeMapper.updateById(couponCodeDO);
                }

                //领取优惠券
                UserCouponDO userCouponDO = new UserCouponDO();
                userCouponDO.setUserId(userId);
                userCouponDO.setCouponId(couponId);
                if (couponDO.getGmtStart() != null && couponDO.getGmtEnd() != null) {
                    //如果优惠券是按区间领取的
                    userCouponDO.setGmtStart(couponDO.getGmtStart());
                    userCouponDO.setGmtEnd(couponDO.getGmtEnd());
                } else if (couponDO.getDays() != null) {
                    //如果是任意领取的，则从当前时间 加上 可用天数
                    userCouponDO.setGmtStart(now);
                    userCouponDO.setGmtEnd(new Date(now.getTime() + 1000l * 60 * 60 * 24 * couponDO.getDays()));
                } else {
                    throw new AppServiceException(ExceptionDefinition.COUPON_STRATEGY_INCORRECT);
                }

                userCouponDO.setGmtUpdate(now);
                userCouponDO.setGmtCreate(now);

                userCouponMapper.insert(userCouponDO);



                return "ok";
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                logger.error("[领取优惠券] 异常", e);
                throw new AppServiceException(ExceptionDefinition.APP_UNKNOWN_EXCEPTION);
            } finally {
                lockComponent.release(COUPON_USER_LOCK + userId + "_" + couponId);
            }
        } else {
            throw new AppServiceException(ExceptionDefinition.SYSTEM_BUSY);
        }

    }

    @Override
    public List<CouponDTO> getObtainableCoupon(Long userId) throws ServiceException {
        List<CouponDTO> couponDOS = couponMapper.getActiveCoupons();
        //活动中的优惠券Id
        List<Long> activeCouponIds = couponDOS.stream().map(couponDO -> couponDO.getId()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(activeCouponIds)) {
            return new ArrayList<>();
        }

        List<KVModel<Long, Long>> userCouponsCount = couponMapper.getUserCouponsCount(userId, activeCouponIds);

        List<CouponDTO> couponDTOList = couponDOS.stream().map(item -> {
            item.setNowCount(0);
            for (int i = 0; i < userCouponsCount.size(); i++) {
                KVModel<Long, Long> kv = userCouponsCount.get(i);
                if (kv != null && kv.getKey().equals(item.getId())) {
                    item.setNowCount(kv.getValue().intValue());
                }
            }
            return item;
        }).collect(Collectors.toList());
        return couponDTOList;
    }

    @Override
    public List<UserCouponDTO> getUserCoupons(Long userId) throws ServiceException {
        return userCouponMapper.getUserCoupons(userId);
    }
}
