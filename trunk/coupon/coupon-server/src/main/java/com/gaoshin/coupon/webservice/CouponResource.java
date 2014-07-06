package com.gaoshin.coupon.webservice;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.gaoshin.coupon.bean.Client;
import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.bean.WebPage;
import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.ShoplocalCrawlHistory;
import com.gaoshin.coupon.entity.ShoplocalResult;
import com.gaoshin.coupon.service.CouponService;
import com.gaoshin.coupon.service.StoreService;
import com.gaoshin.coupon.service.UserService;
import com.sun.jersey.spi.inject.Inject;
import common.util.JacksonUtil;
import common.util.web.BusinessException;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/ws/coupon")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class CouponResource extends JerseyBaseResource {
    @Inject UserService userService;
    @Inject StoreService storeService;
    @Inject CouponService couponService;
    
    @PostConstruct
    public void init() {
        new Thread(){
            public void run() {
                while(true) {
                    try {
                        while(true) {
                            int howmany = couponService.processShoplocalHtml();
                            if(howmany == 0)
                                break;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    try {
                        sleep(10000);
                    }
                    catch (Exception e) {
                    }
                }
            };
        }.start();
    }
    
    @Path("/create")
    @POST
    public Coupon create(Coupon coupon) {
        String userId = assertRequesterUserId();
        couponService.createCoupon(userId, coupon);
        return coupon;
    }
    
    @Path("/enable/{id}")
    @POST
    public GenericResponse enable(@PathParam("id") String id) {
        String userId = assertRequesterUserId();
        couponService.enableCoupon(userId, id);
        return new GenericResponse();
    }
    
    @Path("/disable/{id}")
    @POST
    public GenericResponse disable(@PathParam("id") String id) {
        String userId = assertRequesterUserId();
        couponService.disableCoupon(userId, id);
        return new GenericResponse();
    }
    
    @Path("/remove/{id}")
    @POST
    public GenericResponse remove(@PathParam("id") String id) {
        String userId = assertRequesterUserId();
        couponService.removeCoupon(userId, id);
        return new GenericResponse();
    }
    
    @Path("/details")
    @GET
    public Coupon details(@QueryParam("id") String couponId) throws Exception {
        return couponService.getDetails(couponId);
    }
    
    @Path("/by-geo")
    @GET
    public CouponList byGeo(@QueryParam("latitude") Float lat, @QueryParam("longitude") Float lng
            , @QueryParam("radius") String radius
            , @QueryParam("category") String category
            , @QueryParam("keywords") String keywords
            , @QueryParam("offset") String offset
            , @QueryParam("size") String size
            ) {
        Float fradius = 10f;
        try {
            fradius = Float.parseFloat(radius);
        }
        catch (Exception e) {
        }
        if(fradius > 30)
            fradius = 10f;
        
        int isize = 50;
        try {
            isize = Integer.parseInt(size);
        }
        catch (Exception e) {
        }
        if(isize > 200)
            isize = 50;
        
        int ioffset = 0;
        try {
            ioffset = Integer.parseInt(offset);
        }
        catch (Exception e) {
        }
        
        try {
            return couponService.searchByGeo(lat, lng, fradius, category, keywords, ioffset, isize);
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }
    
    @Path("/crawl-by-geo/{latlng}")
    @GET
    public CouponList crawl(@PathParam("latlng") String latlng) {
        Float lat = Float.parseFloat(latlng.split(",")[0]);
        Float lng = Float.parseFloat(latlng.split(",")[1]);
        try {
            return couponService.crawl(lat, lng);
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }
    
    @Path("/crawl-by-zipcode/{zipcode}")
    @GET
    public CouponList crawlByZipcode(@PathParam("zipcode") String zipcode) {
        try {
            return couponService.crawl(zipcode);
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }
    
    @Path("/stop-shoplocal")
    @POST
    public GenericResponse stopShoplocal() {
        couponService.stopShoplocal();
        return new GenericResponse();
    }
    
    @Path("/start-shoplocal")
    @POST
    public GenericResponse crawlShoplocal() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        couponService.crawlShoplocal();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return new GenericResponse();
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }
    
    @Path("/get-shoplocal-task")
    @POST
    public ShoplocalCrawlHistory getShoplocalTask(Client client) throws Exception {
        ShoplocalCrawlHistory task = couponService.getOneShoplocalTask(client);
        System.out.println("shoplocal task " + JacksonUtil.obj2Json(task));
        if(task != null &&  task.getPage() > 0) {
            task.setUrl(task.getUrl() + "?No=" + task.getPage());
        }
        return task == null ? new ShoplocalCrawlHistory() : task;
    }
    
    
    @Path("/upload-shoplocal-result")
    @POST
    public GenericResponse uploadShoplocalResult(WebPage page) throws Exception {
        couponService.uploadShoplocalResult(page);
        return new GenericResponse();
    }
    
    @GET
    @Path("/list-shoplocal-tasks")
    public List<ShoplocalCrawlHistory> listShoplocalHistory(@QueryParam("status") String status, @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("100") int size ) {
        if("".equals(status))
            status = null;
        return couponService.listShoplocalHistory(status, offset, size);
    }
    
    @GET
    @Path("/shoplocal-result")
    public ShoplocalResult getShoplocalResult(@QueryParam("id") String taskid, @QueryParam("page") int page) {
        return couponService.getShoplocalResult(taskid, page);
    }
    
    @GET
    @Path("/list-shoplocal-results")
    public List<ShoplocalResult> listShoplocalResults(@QueryParam("id") String taskid) {
        return couponService.listShoplocalResults(taskid);
    }
}
