package com.gaoshin.coupon.webservice;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.entity.User;
import com.gaoshin.coupon.entity.UserType;

public class CouponTest extends CouponeTestBase {
    @Test
    public void createCoupon() throws Exception {
        User user0 = register(UserType.Store);
        
        Store store = new Store();
        store.setAddress(getCurrentTimeMillisString());
        store.setCity("city");
        store.setState("state");
        store.setCountry("country");
        store.setName("name");
        store.setZipcode("zipcode");
        store.setPhone("phone");
        store.setEmail("email");
        Store created = getBuilder("/ws/store/add").post(Store.class, store);
        
        Store branch0 = new Store();
        branch0.setAddress(getCurrentTimeMillisString());
        branch0.setCity("city");
        branch0.setState("state");
        branch0.setCountry("country");
        branch0.setName("name");
        branch0.setZipcode("zipcode");
        branch0.setPhone("phone");
        branch0.setEmail("email");
        branch0.setParentId(created.getId());
        Store branchCreated0 = getBuilder("/ws/store/add").post(Store.class, branch0);
        
        Store branch1 = new Store();
        branch1.setAddress(getCurrentTimeMillisString());
        branch1.setCity("city");
        branch1.setState("state");
        branch1.setCountry("country");
        branch1.setName("name");
        branch1.setZipcode("zipcode");
        branch1.setPhone("phone");
        branch1.setEmail("email");
        branch1.setParentId(created.getId());
        Store branchCreated1 = getBuilder("/ws/store/add").post(Store.class, branch1);

        Coupon coupon = new Coupon();
        coupon.setDescription("description");
        coupon.setExpire(System.currentTimeMillis() + 30l*24l*3600000l);
        coupon.setStoreId(branchCreated1.getId());
        coupon.setTitle("title");
        Coupon couponCreated = getBuilder("/ws/coupon/create").post(Coupon.class, coupon);

        Coupon coupon1 = new Coupon();
        coupon1.setDescription("description");
        coupon1.setExpire(System.currentTimeMillis() + 30l*24l*3600000l);
        coupon1.setStoreId(created.getId());
        coupon1.setTitle("title");
        Coupon couponCreated1 = getBuilder("/ws/coupon/create").post(Coupon.class, coupon1);
        
        {
            CouponList list0 = getBuilder("/ws/store/coupons", "id", created.getId()).get(CouponList.class);
            Assert.assertEquals(1, list0.getItems().size());
            
            CouponList list1 = getBuilder("/ws/store/coupons", "id", branchCreated0.getId()).get(CouponList.class);
            Assert.assertEquals(0, list1.getItems().size());
            
            CouponList list2 = getBuilder("/ws/store/coupons", "id", branchCreated1.getId()).get(CouponList.class);
            Assert.assertEquals(1, list2.getItems().size());
        }
        
        getBuilder("/ws/coupon/enable/" + couponCreated1.getId()).post(" ");

        {
            CouponList list0 = getBuilder("/ws/store/coupons", "id", created.getId()).get(CouponList.class);
            Assert.assertEquals(1, list0.getItems().size());
            
            CouponList list1 = getBuilder("/ws/store/coupons", "id", branchCreated0.getId()).get(CouponList.class);
            Assert.assertEquals(1, list1.getItems().size());
            
            CouponList list2 = getBuilder("/ws/store/coupons", "id", branchCreated1.getId()).get(CouponList.class);
            Assert.assertEquals(2, list2.getItems().size());
        }
        
        getBuilder("/ws/coupon/remove/" + couponCreated1.getId()).post(" ");

        {
            CouponList list0 = getBuilder("/ws/store/coupons", "id", created.getId()).get(CouponList.class);
            Assert.assertEquals(0, list0.getItems().size());
            
            CouponList list1 = getBuilder("/ws/store/coupons", "id", branchCreated0.getId()).get(CouponList.class);
            Assert.assertEquals(0, list1.getItems().size());
            
            CouponList list2 = getBuilder("/ws/store/coupons", "id", branchCreated1.getId()).get(CouponList.class);
            Assert.assertEquals(1, list2.getItems().size());
        }
    }
}
