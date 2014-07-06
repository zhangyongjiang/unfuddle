package com.gaoshin.coupon.webservice;

import java.util.HashSet;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.gaoshin.coupon.bean.CategoryList;
import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.bean.StoreAndCoupon;
import com.gaoshin.coupon.bean.StoreAndCouponList;
import com.gaoshin.coupon.bean.StoreList;
import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.entity.User;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private JAXBContext context;
    private HashSet<Class> allTypes = new HashSet<Class>();
    private Class[] types = {
            User.class,
            Store.class,
            StoreList.class,
            StoreTree.class,
            Coupon.class,
            CouponList.class,
            StoreAndCoupon.class,
            StoreAndCouponList.class,
            CategoryList.class,
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
