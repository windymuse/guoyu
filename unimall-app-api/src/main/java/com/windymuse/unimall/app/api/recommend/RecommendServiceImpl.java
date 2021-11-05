package com.windymuse.unimall.app.api.recommend;

import com.windymuse.unimall.biz.service.recommend.RecommendBizService;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.dto.RecommendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-08
 * Time: 下午3:40
 */
@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendBizService recommendBizService;

    @Override
    public List<RecommendDTO> getRecommendByType(Integer recommendType,Integer pageNo,Integer pageSize) throws ServiceException {
        return recommendBizService.getRecommendByType(recommendType,pageNo,pageSize);
    }
}
