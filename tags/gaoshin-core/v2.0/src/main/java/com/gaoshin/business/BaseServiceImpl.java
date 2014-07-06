package com.gaoshin.business;

import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import org.codehaus.jackson.map.ObjectMapper;

import common.web.BusinessException;
import common.web.ServiceError;

public class BaseServiceImpl {
    private static Validator validator = new Validator();
    private static ObjectMapper jsonProcessor = new ObjectMapper();

    protected void verifyBeans(Object bean) {
        List<ConstraintViolation> validate = validator.validate(bean);
        try {
            String data = jsonProcessor.writeValueAsString(validate);
            throw new BusinessException(ServiceError.InvalidInput, data);
        } catch (Exception e) {
        }
    }

    protected void verifyBeans(Object bean, String... profiles) {
        List<ConstraintViolation> validate = validator.validate(bean, profiles);
        if (validate.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation vio : validate) {
                sb.append(vio.toString()).append("\n");
            }
            throw new BusinessException(ServiceError.InvalidInput, sb.toString());
        }
    }
}
