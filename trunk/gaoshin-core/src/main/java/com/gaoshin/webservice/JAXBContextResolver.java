package com.gaoshin.webservice;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.gaoshin.amazon.AwsBrowseNodeList;
import com.gaoshin.beans.CategoryList;
import com.gaoshin.beans.DimensionValueList;
import com.gaoshin.beans.GroupList;
import com.gaoshin.beans.LocationList;
import com.gaoshin.beans.MessageList;
import com.gaoshin.beans.NotificationList;
import com.gaoshin.beans.ObjectBeanList;
import com.gaoshin.beans.PostList;
import com.gaoshin.beans.UserAndDistanceList;
import com.gaoshin.dating.SearchCriteriaList;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private JAXBContext context;
    private Class[] types = { CategoryList.class, ObjectBeanList.class,
            LocationList.class, AwsBrowseNodeList.class,
            DimensionValueList.class, MessageList.class, UserAndDistanceList.class,
            NotificationList.class, GroupList.class, PostList.class, SearchCriteriaList.class
            };

    public JAXBContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration
                .mapped()
                .arrays("list", "children", "items").build(), types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        for (Class type : types) {
            if (type.equals(objectType)) {
                return context;
            }
        }
        return null;
    }
}
