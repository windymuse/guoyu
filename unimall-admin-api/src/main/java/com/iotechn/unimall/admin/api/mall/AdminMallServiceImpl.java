package com.iotechn.unimall.admin.api.mall;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.MallDO;
import com.iotechn.unimall.data.mapper.MallMapper;
import com.iotechn.unimall.data.model.Page;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.s
 * Description:
 * Mall: kbq
 * Date: 2019-07-11
 * Time: 下午7:57
 */
@Service
public class AdminMallServiceImpl implements AdminMallService {

    @Autowired
    private MallMapper mallMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMall(Long adminId, MallDO mall) throws ServiceException {
        if (mall == null){
            throw new AdminServiceException(ExceptionDefinition.MALL_INFORMATION_MISSING);
        }
        validateCheck(mall);
        if(mallMapper.selectCount(new EntityWrapper<MallDO>().eq("title",mall.getTitle())) > 0){
            throw new AdminServiceException(ExceptionDefinition.MALL_NAME_EXIST);
        }
        Date now = new Date();
        mall.setId(null);
        mall.setGmtCreate(now);
        mall.setGmtUpdate(now);
        return mallMapper.insert(mall) > 0;
    }

    private void validateCheck(MallDO mall) throws AdminServiceException {
        if(mall.getTitle() == null){
            throw new AdminServiceException(ExceptionDefinition.MALL_INFORMATION_MISSING);
        }
        if(mall.getLongitude() == null){
            throw new AdminServiceException(ExceptionDefinition.MALL_INFORMATION_MISSING);
        }
        if(mall.getLatitude() == null){
            throw new AdminServiceException(ExceptionDefinition.MALL_INFORMATION_MISSING);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMall(Long adminId, Long id, String name) throws ServiceException {
        return mallMapper.delete(new EntityWrapper<MallDO>()
                .eq("id", id)
                .eq("name",name)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMall(Long adminId, MallDO mall) throws ServiceException {
        if (mall == null || mall.getId() == null){
            throw new AdminServiceException(ExceptionDefinition.MALL_INFORMATION_MISSING);
        }
        validateCheck(mall);
        Date now = new Date();
        mall.setGmtUpdate(now);
        MallDO mallDO  = mallMapper.selectById(mall.getId());
        if(mallDO.getTitle() != null){
            return mallMapper.updateById(mall) > 0;
        }
        return mallMapper.updateById(mall) > 0;
    }

    @Override
    public Page<MallDO> getMall(Long adminId, Long id, String title, String description, Integer showType, Integer pageNo, Integer limit) throws ServiceException {
        EntityWrapper wrapper = new EntityWrapper();
        if (id != null) {
            wrapper.eq("id", id);
        }
        if (title != null) {
            wrapper.like("title", title);
        }
        if (description != null) {
            wrapper.like("description", description);
        }
        if (showType != null) {
            wrapper.eq("show_type", showType);
        }
        wrapper.orderBy("title");
        Integer count = mallMapper.selectCount(wrapper);

        List<MallDO> mallDOList = mallMapper.selectPage(new RowBounds((pageNo - 1) * limit, limit), wrapper);

        Page<MallDO> page = new Page<>(mallDOList, pageNo, limit, count);
        return page;

    }


}
