package com.windymuse.unimall.admin.api.freight;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.windymuse.unimall.biz.service.goods.GoodsBizService;
import com.windymuse.unimall.core.exception.AdminServiceException;
import com.windymuse.unimall.core.exception.ExceptionDefinition;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.component.CacheComponent;
import com.windymuse.unimall.data.domain.FreightTemplateCarriageDO;
import com.windymuse.unimall.data.domain.FreightTemplateDO;
import com.windymuse.unimall.data.domain.SpuDO;
import com.windymuse.unimall.data.dto.freight.FreightTemplateDTO;
import com.windymuse.unimall.data.mapper.FreightTemplateCarriageMapper;
import com.windymuse.unimall.data.mapper.FreightTemplateMapper;
import com.windymuse.unimall.data.mapper.SpuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-07
 * Time: 下午4:25
 */
@Service
public class AdminFreightTemplateServiceImpl implements AdminFreightTemplateService {

    @Autowired
    private FreightTemplateMapper freightTemplateMapper;

    @Autowired
    private FreightTemplateCarriageMapper freightTemplateCarriageMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CacheComponent cacheComponent;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFreightTemplate(String templateName, String spuLocation, Integer deliveryDeadline, Integer defaultFreePrice, Integer deliverDistanceFree, Integer deliverDistanceMax, Integer defaultFirstNum, Integer defaultFirstPrice, Integer defaultContinueNum, Integer defaultContinuePrice, List freightTemplateCarriageDOList, Long adminId) throws ServiceException {
        Date now = new Date();
        FreightTemplateDO freightTemplateDO = new FreightTemplateDO(templateName, spuLocation, deliveryDeadline, defaultFreePrice, deliverDistanceFree, deliverDistanceMax, defaultFirstNum, defaultFirstPrice, defaultContinueNum, defaultContinuePrice);
        freightTemplateDO.setGmtCreate(now);
        freightTemplateDO.setGmtUpdate(now);
        int judgeSQL = freightTemplateMapper.insert(freightTemplateDO); //插入模板主表
        if (judgeSQL <= 0) {
            throw new AdminServiceException(ExceptionDefinition.FREIGHT_TEMPLATE_INSERT_FAILED);
        }
        if (CollectionUtils.isEmpty(freightTemplateCarriageDOList)) {
            return true;
        }
        List<FreightTemplateCarriageDO> collect = (List<FreightTemplateCarriageDO>) freightTemplateCarriageDOList.stream().map(item -> {
            FreightTemplateCarriageDO t = JSONObject.toJavaObject((JSON) item, FreightTemplateCarriageDO.class);
            return t;
        }).collect(Collectors.toList());
        insertFreightTemplateCarriage(freightTemplateDO, collect, now);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFreightTemplate(Long templateId, Long adminId) throws ServiceException {
        if (spuMapper.selectCount(new EntityWrapper<SpuDO>().eq("freight_template_id", templateId)) > 0) {
            throw new AdminServiceException(ExceptionDefinition.FREIGHT_SPU_QUERY_HAS);
        }
        Integer num = freightTemplateMapper.deleteById(templateId);
        if (num <= 0) {
            throw new AdminServiceException(ExceptionDefinition.FREIGHT_TEMPLATE_DELETE_FAILED);
        }
        freightTemplateCarriageMapper.delete(
                new EntityWrapper<FreightTemplateCarriageDO>()
                        .eq("template_id", templateId));
        cacheComponent.delPrefixKey(GoodsBizService.CA_SPU_PREFIX);
//        throw new AdminServiceException(ExceptionDefinition.FREIGHT_TEMPLATE_UPDATE_FAILED);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFreightTemplate(Long templateId, String templateName, String spuLocation, Integer deliveryDeadline, Integer defaultFreePrice, Integer deliverDistanceFree, Integer deliverDistanceMax, Integer defaultFirstNum, Integer defaultFirstPrice, Integer defaultContinueNum, Integer defaultContinuePrice, List freightTemplateCarriageDOList, Long adminId) throws ServiceException {
        Date now = new Date();
        FreightTemplateDO freightTemplateDO = new FreightTemplateDO(templateName, spuLocation, deliveryDeadline, defaultFreePrice, deliverDistanceFree, deliverDistanceMax, defaultFirstNum, defaultFirstPrice, defaultContinueNum, defaultContinuePrice);
        freightTemplateDO.setId(templateId);
        freightTemplateDO.setGmtUpdate(now);
        if (freightTemplateMapper.updateById(freightTemplateDO) <= 0) {    //如果主表修改失败
            throw new AdminServiceException(ExceptionDefinition.FREIGHT_TEMPLATE_UPDATE_FAILED);
        }
        freightTemplateCarriageMapper.delete(new EntityWrapper<FreightTemplateCarriageDO>().eq("template_id", templateId));
        if (CollectionUtils.isEmpty(freightTemplateCarriageDOList)) {
            return true;
        }
        List<FreightTemplateCarriageDO> collect = (List<FreightTemplateCarriageDO>) freightTemplateCarriageDOList.stream().map(item -> {
            FreightTemplateCarriageDO t = JSONObject.toJavaObject((JSON) item, FreightTemplateCarriageDO.class);
            return t;
        }).collect(Collectors.toList());
        insertFreightTemplateCarriage(freightTemplateDO, collect, now);
        cacheComponent.delPrefixKey(GoodsBizService.CA_SPU_PREFIX);
        return true;
    }

    private void insertFreightTemplateCarriage(FreightTemplateDO freightTemplateDO, List<FreightTemplateCarriageDO> freightTemplateCarriageDOList, Date now) throws ServiceException {
        //表中设定可默认值，所以就不检查是否为空
        for (FreightTemplateCarriageDO freightTemplateCarriageDO : freightTemplateCarriageDOList) {
            freightTemplateCarriageDO.setTemplateId(freightTemplateDO.getId());
            freightTemplateCarriageDO.setGmtCreate(now);
            freightTemplateCarriageDO.setGmtUpdate(now);
            Integer judgeSQL = freightTemplateCarriageMapper.insert(freightTemplateCarriageDO);
            if (judgeSQL <= 0) {
                throw new AdminServiceException(ExceptionDefinition.FREIGHT_CARRIAGE_INSERT_FAILED);
            }
        }
    }

    @Override
    public List<FreightTemplateDTO> getAllFreightTemplate(Long adminId) throws ServiceException {
        List<FreightTemplateDO> freightTemplateDOList = freightTemplateMapper.selectList(null); //查出主表所有数据
        List<FreightTemplateDTO> freightTemplateDTOList = new ArrayList<>();
        if (freightTemplateDOList == null || freightTemplateDOList.size() == 0) { //如果主表没有记录
            return freightTemplateDTOList;
        }
        for (FreightTemplateDO freightTemplateDO : freightTemplateDOList) {  //查出副表中，主表每条数据对应的数据
            FreightTemplateDTO freightTemplateDTO = new FreightTemplateDTO();
            List<FreightTemplateCarriageDO> freightTemplateCarriageDOList = freightTemplateCarriageMapper.selectList(new EntityWrapper<FreightTemplateCarriageDO>()
                    .eq("template_id", freightTemplateDO.getId()));
            freightTemplateDTO.setFreightTemplateDO(freightTemplateDO);
            freightTemplateDTO.setFreightTemplateCarriageDOList(freightTemplateCarriageDOList);
            freightTemplateDTOList.add(freightTemplateDTO);
        }
        return freightTemplateDTOList;
    }


}
