package com.gaoshin.webservice;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

@Path("/privacy")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class PrivacyResource extends PredefinedGroupResource {
    public static final String PrivacyGroupName = "Privacy";

    public PrivacyResource() {
        super(PrivacyGroupName);
    }

}
