package com.windymuse.unimall.biz.service.freight;

import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.dto.freight.ShipTraceDTO;

/**
 * Created by rize on 2019/7/10.
 */
public interface ShipTraceQuery {

    public ShipTraceDTO query(String shipNo, String shipCode) throws ServiceException;

}
