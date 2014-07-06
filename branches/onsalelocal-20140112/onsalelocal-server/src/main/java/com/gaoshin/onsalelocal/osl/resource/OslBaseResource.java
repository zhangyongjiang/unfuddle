package com.gaoshin.onsalelocal.osl.resource;

import com.gaoshin.onsalelocal.osl.logging.MdcKeys;
import com.gaoshin.onsalelocal.osl.logging.RequestId;
import common.util.web.BusinessException;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

public class OslBaseResource extends JerseyBaseResource {
	protected String getUserId() {
        String reqid = requestInvoker.get().getHeader(MdcKeys.Reqid.name());
        if(reqid != null) {
            RequestId req = new RequestId(reqid);
            return req.getUserId();
        }
		return null;
	}
	
	protected String assertUserId() {
		String userId = getUserId();
		if(userId == null)
			throw new BusinessException(ServiceError.NoGuest);
		return userId;
	}
}
