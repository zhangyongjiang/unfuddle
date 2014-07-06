package com.gaoshin.coupon.webservice;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.bean.StoreList;
import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.service.CouponService;
import com.gaoshin.coupon.service.StoreService;
import com.gaoshin.coupon.service.UserService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/store")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class StoreResource extends JerseyBaseResource {
    @Inject UserService userService;
    @Inject StoreService storeService;
    @Inject CouponService couponService;

    @PostConstruct
    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    int howmany = storeService.geocodeOneStore();
                    try {
                        Thread.sleep((howmany == 0) ? 10000 : 2000);
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }
    
    @Path("add")
    @POST
    public Store add(Store store) {
        String userId = assertRequesterUserId();
        storeService.addStore(userId, store);
        return store;
    }
    
    @GET
    public Store get(@QueryParam("id") String id) {
        Store store = storeService.getStore(id);
        return store;
    }
    
    @GET
    @Path("branch-tree")
    public StoreTree getBranchTree(@QueryParam("id") String id) {
        StoreTree store = storeService.getBranchTree(id);
        return store;
    }
    
    @GET
    @Path("branches")
    public StoreList getBranches(@QueryParam("id") String id) {
        StoreList store = storeService.getBranches(id);
        return store;
    }
    
    @GET
    @Path("tops")
    public StoreList listStores() {
        String userId = assertRequesterUserId();
        StoreList store = storeService.listTopStores(userId);
        return store;
    }
    
    @GET
    @Path("coupons")
    public CouponList listCoupons(@QueryParam("id") String id) {
        CouponList list = storeService.listCoupons(id);
        return list;
    }
    
    @POST
    @Path("create-walmart-stores") 
    public GenericResponse createWalmartStores() throws IOException {
        storeService.createWalmartStores();
        return new GenericResponse();
    }
    
    @POST
    @Path("create-target-stores") 
    public GenericResponse createTargetStores() throws IOException {
        storeService.createTargetStores();
        return new GenericResponse();
    }
    
    @POST
    @Path("create-cvs-stores") 
    public GenericResponse createCvsStores() throws IOException {
        storeService.createCvsStores();
        return new GenericResponse();
    }
    
    @POST
    @Path("create-safeway-stores") 
    public GenericResponse createSafewayStores() throws IOException {
        storeService.createSafewayStores();
        return new GenericResponse();
    }
    
    @POST
    @Path("create-shoplocal-stores")
    public GenericResponse createShoplocalStores() throws IOException {
        storeService.createShoplocalStores();
        return new GenericResponse();
    }
}
