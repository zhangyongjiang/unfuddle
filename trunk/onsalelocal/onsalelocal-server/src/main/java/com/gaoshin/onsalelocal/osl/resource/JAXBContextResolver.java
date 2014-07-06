package com.gaoshin.onsalelocal.osl.resource;

import java.util.HashSet;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.nextshopper.api.OfferDetailsList;
import com.nextshopper.api.OfferList;
import com.nextshopper.api.StoreList;
import com.nextshopper.osl.entity.FavouriteOfferDetailsList;
import com.nextshopper.osl.entity.FavouriteStoreDetailsList;
import com.nextshopper.osl.entity.FollowDetailsList;
import com.nextshopper.osl.entity.OfferCommentDetailsList;
import com.nextshopper.osl.entity.StoreCommentDetailsList;
import com.nextshopper.osl.entity.TagList;
import com.nextshopper.osl.entity.UserList;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private JAXBContext context;
    private HashSet<Class> allTypes = new HashSet<Class>();
    private Class[] types = {
    		StoreList.class,
    		OfferList.class,
    		OfferDetailsList.class,
    		TagList.class,
    		FavouriteOfferDetailsList.class,
    		UserList.class,
    		OfferCommentDetailsList.class,
    		FollowDetailsList.class,
    		StoreCommentDetailsList.class,
    		FavouriteStoreDetailsList.class,
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
