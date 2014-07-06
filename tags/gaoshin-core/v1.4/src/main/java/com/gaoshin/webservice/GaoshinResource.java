package com.gaoshin.webservice;

import com.gaoshin.beans.User;
import com.gaoshin.business.Setup;
import com.sun.jersey.spi.inject.Inject;
import common.web.BusinessException;
import common.web.JerseyBaseResource;
import common.web.ServiceError;

public class GaoshinResource extends JerseyBaseResource {
    @Inject
    private Setup setup;

    protected User getUser() {
        UserResource userResource = resourceContext.getResource(UserResource.class);
        User user = userResource.getCurrentUser();
        return user;
	}
	
    protected User assertUser() {
        User user = getUser();
        if (user == null)
            throw new BusinessException(ServiceError.Forbidden);

        return user;
    }
}
