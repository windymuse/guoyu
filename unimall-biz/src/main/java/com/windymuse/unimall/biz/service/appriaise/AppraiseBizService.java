package com.windymuse.unimall.biz.service.appriaise;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.windymuse.unimall.core.Const;
import com.windymuse.unimall.core.exception.AppServiceException;
import com.windymuse.unimall.core.exception.ExceptionDefinition;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.component.CacheComponent;
import com.windymuse.unimall.data.domain.AppraiseDO;
import com.windymuse.unimall.data.dto.appraise.AppraiseResponseDTO;
import com.windymuse.unimall.data.enums.BizType;
import com.windymuse.unimall.data.mapper.AppraiseMapper;
import com.windymuse.unimall.data.mapper.ImgMapper;
import com.windymuse.unimall.data.mapper.OrderMapper;
import com.windymuse.unimall.data.mapper.OrderSkuMapper;
import com.windymuse.unimall.data.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rize on 2019/7/13.
 */
@Service
public class AppraiseBizService {

    @Autowired
    private AppraiseMapper appraiseMapper;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private ImgMapper imgMapper;

    public static final String CA_APPRAISE_KEY = "CA_APPRAISE_";

    public Page<AppraiseResponseDTO> getSpuAllAppraise(Long spuId, Integer pageNo, Integer pageSize) throws ServiceException {
        String cacheKey = CA_APPRAISE_KEY + spuId + "_" + pageNo + "_" + pageSize;
        Page obj = cacheComponent.getObj(cacheKey, Page.class);
        if (obj != null) {
            return obj;
        }
        Integer count = appraiseMapper.selectCount(new EntityWrapper<AppraiseDO>().eq("spu_id", spuId));
        Integer offset = pageSize * (pageNo - 1);
        List<AppraiseResponseDTO> appraiseResponseDTOS = appraiseMapper.selectSpuAllAppraise(spuId, offset, pageSize);
        for (AppraiseResponseDTO appraiseResponseDTO : appraiseResponseDTOS) {
            appraiseResponseDTO.setImgList(imgMapper.getImgs(BizType.COMMENT.getCode(), appraiseResponseDTO.getId()));
        }
        Page<AppraiseResponseDTO> page = new Page<>(appraiseResponseDTOS, pageNo, pageSize, count);
        cacheComponent.putObj(cacheKey, page, Const.CACHE_ONE_DAY);
        return page;
    }

}
