package com.gaoshin.coupon.service.impl;

import common.util.web.BusinessException;
import common.util.web.ServiceError;

public class ServiceBase {
    protected void assertNotNull(Object obj) {
        if(obj == null)
            throw new BusinessException(ServiceError.InvalidInput);
    }
}
