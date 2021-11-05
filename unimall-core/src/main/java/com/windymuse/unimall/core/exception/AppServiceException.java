package com.windymuse.unimall.core.exception;

import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.core.exception.ServiceExceptionDefinition;

/**
 * Created by rize on 2019/7/1.
 */
public class AppServiceException extends ServiceException {

    public AppServiceException(ServiceExceptionDefinition definition) {
        super(definition);
    }

    public AppServiceException(String message, int code) {
        super(message,code);
    }
}
