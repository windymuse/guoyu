package com.windymuse.unimall.data.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.windymuse.unimall.data.domain.CartDO;
import com.windymuse.unimall.data.dto.CartDTO;

import java.util.List;

/**
 * Created by rize on 2019/7/3.
 */
public interface CartMapper extends BaseMapper<CartDO> {

    public int countCart(Long userId);

    public List<CartDTO> getCartList(Long userId);

}
