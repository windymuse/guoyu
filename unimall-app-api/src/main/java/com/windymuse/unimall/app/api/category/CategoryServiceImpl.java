package com.windymuse.unimall.app.api.category;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.windymuse.unimall.biz.service.category.CategoryBizService;
import com.windymuse.unimall.core.Const;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.component.CacheComponent;
import com.windymuse.unimall.data.domain.CategoryDO;
import com.windymuse.unimall.data.dto.CategoryDTO;
import com.windymuse.unimall.data.mapper.CategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by rize on 2019/7/2.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryBizService categoryBizService;

    @Override
    public List<CategoryDTO> categoryList() throws ServiceException {
        return categoryBizService.categoryList();
    }

    /**
     * @param categoryId
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Long> getCategoryFamily(Long categoryId) throws ServiceException {
        return categoryBizService.getCategoryFamily(categoryId);
    }
}
