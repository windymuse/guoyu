package com.windymuse.unimall.app.api.groupshop;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.dto.goods.GroupShopDTO;
import com.windymuse.unimall.data.mapper.GroupShopMapper;
import com.windymuse.unimall.data.mapper.GroupShopSkuMapper;
import com.windymuse.unimall.data.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.ServerException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: rize
 * Date: 2019/11/24
 * Time: 15:26
 */
@Service
public class GroupShopServiceImpl implements GroupShopService {

    @Autowired
    private GroupShopMapper groupShopMapper;

    @Override
    public Page<GroupShopDTO> getGroupShopPage(Integer pageNo, Integer pageSize) throws ServerException {
        Integer count = groupShopMapper.selectCount(null);
        List<GroupShopDTO> groupShopPage = groupShopMapper.getGroupShopPage((pageNo - 1) * pageSize, pageSize);
        return new Page<>(groupShopPage, pageNo, pageSize, count);
    }


}
