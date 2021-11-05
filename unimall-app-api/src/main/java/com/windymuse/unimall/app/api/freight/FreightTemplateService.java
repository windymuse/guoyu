package com.windymuse.unimall.app.api.freight;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.dto.order.OrderRequestDTO;
import com.windymuse.unimall.data.mapper.FreightTemplateCarriageMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-07
 * Time: 下午7:40
 */
@HttpOpenApi(group = "freight", description = "运费计算api")
public interface FreightTemplateService {

    @HttpMethod(description = "得到订单的运费")
    public Integer getFreightMoney(
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户ID") Long userId,
            @NotNull @HttpParam(name = "orderRequestDTO", type = HttpParamType.COMMON, description = "订单传回数据") OrderRequestDTO orderRequestDTO) throws ServiceException;
}
