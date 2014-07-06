package com.gaoshin.onsalelocal.yipit.resource;

import java.util.HashSet;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private JAXBContext context;
    private HashSet<Class> allTypes = new HashSet<Class>();
    private Class[] types = {
            };

    public JAXBContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration
                .mapped()
                .arrays("list", "children", "items", "scenes", "msgs",
                        "values", "ucmList", "members", "attrNames",
                        "friends", "checkins", "pageFans", "pages", "dailyHours").build(),
                types);
        for (Class type : types) {
            allTypes.add(type);
        }
    }

    public JAXBContext getContext(Class<?> objectType) {
        if(allTypes.contains(objectType))
            return context;
        else
            return null;
    }
}
