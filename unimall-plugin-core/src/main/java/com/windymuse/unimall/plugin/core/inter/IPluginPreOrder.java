package com.windymuse.unimall.plugin.core.inter;

import com.windymuse.unimall.data.dto.order.OrderRequestDTO;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: rize
 * Date: 2019/10/24
 * Time: 11:22
 */
public interface IPluginPreOrder {

    public OrderRequestDTO invoke(OrderRequestDTO requestDTO);

}
