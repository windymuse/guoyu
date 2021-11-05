package com.windymuse.unimall.app.api.integral;

import com.windymuse.unimall.app.api.advertisement.AdvertisementService;
import com.windymuse.unimall.biz.service.recommend.RecommendBizService;
import com.windymuse.unimall.biz.service.goods.GoodsBizService;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.AdvertisementDO;
import com.windymuse.unimall.data.dto.AdvertisementDTO;
import com.windymuse.unimall.data.dto.IntegralIndexDataDTO;
import com.windymuse.unimall.data.dto.RecommendDTO;
import com.windymuse.unimall.data.dto.goods.SpuDTO;
import com.windymuse.unimall.data.enums.AdvertisementType;
import com.windymuse.unimall.data.enums.RecommendType;
import com.windymuse.unimall.data.model.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rize on 2019/7/14.
 */
@Service
public class IntegralServiceImpl implements IntegralService {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private GoodsBizService goodsBizService;

    @Autowired
    private RecommendBizService recommendBizService;

    @Override
    public IntegralIndexDataDTO getIndexData() throws ServiceException {
        //分类
        List<AdvertisementDO> activeAd = advertisementService.getActiveAd(null);
        Map<String, List<AdvertisementDTO>> adDTOMap = activeAd.stream().map(item -> {
            AdvertisementDTO advertisementDTO = new AdvertisementDTO();
            BeanUtils.copyProperties(item, advertisementDTO);
            return advertisementDTO;
        }).collect(Collectors.groupingBy(item -> "t" + item.getAdType()));
        List<AdvertisementDTO> categoryPickAd = adDTOMap.get("t" + AdvertisementType.CATEGORY_PICK.getCode());
        //封装 分类精选 商品
        if (!CollectionUtils.isEmpty(categoryPickAd)) {
            for (AdvertisementDTO item : categoryPickAd) {
                Page<SpuDTO> pickPage = goodsBizService.getGoodsPage(1, 10, new Long(item.getUrl().substring(item.getUrl().lastIndexOf("=") + 1)), "sales", false,null);
                item.setData(pickPage.getItems());
            }
        }
        IntegralIndexDataDTO integralIndexDataDTO = new IntegralIndexDataDTO();
        integralIndexDataDTO.setAdvertisement(adDTOMap);

        /**
         * 橱窗推荐
         */
        List<RecommendDTO> windowRecommend = recommendBizService.getRecommendByType(RecommendType.WINDOW.getCode(), 1, 10);
        integralIndexDataDTO.setWindowRecommend(windowRecommend);

        /**
         * 销量冠军
         */
        List<SpuDTO> salesTop = goodsBizService.getGoodsPage(1, 8, null, "sales", false, null).getItems();
        integralIndexDataDTO.setSalesTop(salesTop);

        /**
         * 最近上新
         */
        List<SpuDTO> newTop = goodsBizService.getGoodsPage(1, 8, null, "id", false, null).getItems();
        integralIndexDataDTO.setNewTop(newTop);
        return integralIndexDataDTO;
    }

}
