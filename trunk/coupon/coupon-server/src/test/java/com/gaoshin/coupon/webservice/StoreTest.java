package com.gaoshin.coupon.webservice;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.coupon.bean.StoreList;
import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.entity.User;
import com.gaoshin.coupon.entity.UserType;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.util.web.ServiceError;

public class StoreTest extends CouponeTestBase {
    @Test
    public void createStore() {
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
        
        Store indb = getBuilder("/ws/store", "id", created.getId()).get(Store.class);
        Assert.assertEquals(store.getAddress(), indb.getAddress());
        
        Store store1 = new Store();
        store1.setAddress(getCurrentTimeMillisString());
        store1.setCity("city");
        store1.setState("state");
        store1.setCountry("country");
        store1.setName("name");
        store1.setZipcode("zipcode");
        store1.setPhone("phone");
        store1.setEmail("email");
        getBuilder("/ws/store/add").post(Store.class, store1);
        
        StoreList list = getBuilder("/ws/user/my-stores").get(StoreList.class);
        int cnt = 0;
        for(Store m : list.getItems()) {
            if(m.getAddress().equals(store.getAddress()))
                cnt++;
            if(m.getAddress().equals(store1.getAddress()))
                cnt++;
        }
        Assert.assertEquals(2, cnt);
        
        Store branch = new Store();
        branch.setAddress(getCurrentTimeMillisString());
        branch.setCity("city");
        branch.setState("state");
        branch.setCountry("country");
        branch.setName("name");
        branch.setZipcode("zipcode");
        branch.setPhone("phone");
        branch.setEmail("email");
        branch.setParentId(created.getId());
        getBuilder("/ws/store/add").post(Store.class, branch);
        
        StoreTree tree = getBuilder("/ws/store/branch-tree", "id", created.getId()).get(StoreTree.class);
        Assert.assertEquals(1, tree.getChildren().size());
        Assert.assertEquals(branch.getAddress(), tree.getChildren().get(0).getAddress());
        
        logout();
        try {
            getBuilder("/ws/store/add").post(Store.class, store);
            Assert.assertTrue(false);
        }
        catch (UniformInterfaceException e) {
            Assert.assertEquals(ServiceError.NoGuest.getErrorCode(), e.getResponse().getStatus());
        }
        
        User user1 = register(UserType.Store);
        try {
            getBuilder("/ws/store/add").post(Store.class, branch);
            Assert.assertTrue(false);
        }
        catch (UniformInterfaceException e) {
            Assert.assertEquals(ServiceError.Forbidden.getErrorCode(), e.getResponse().getStatus());
        }
    }
    
    @Test
    public void testImportShoplocalStores() throws Exception {
        getBuilder("/ws/location/set-location/CA").get(String.class);
        getBuilder("/ws/store/create-shoplocal-stores").post(" ");
        Thread.sleep(100000);
    }
}
