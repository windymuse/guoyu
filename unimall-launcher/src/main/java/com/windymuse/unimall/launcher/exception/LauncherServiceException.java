package com.windymuse.unimall.launcher.exception;

import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.core.exception.ServiceExceptionDefinition;

/**
 * Created by rize on 2019/6/30.
 */
public class LauncherServiceException extends ServiceException {

    public LauncherServiceException(ServiceExceptionDefinition exceptionDefinition) {
        super(exceptionDefinition);
    }

}
