package com.gaoshin.coupon.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.bean.StoreList;
import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.dao.CouponDao;
import com.gaoshin.coupon.dao.StoreDao;
import com.gaoshin.coupon.dao.UserDao;
import com.gaoshin.coupon.entity.AccountStatus;
import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.entity.UserStoreAccount;
import com.gaoshin.coupon.service.StoreService;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import common.util.PhoneNumberUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class StoreServiceImpl extends ServiceBase implements StoreService {
    @Autowired private UserDao userDao;
    @Autowired private StoreDao storeDao;
    @Autowired private CouponDao couponDao;

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void addStore(String userId, Store store) {
        store.setId(null);
        String parentId = store.getParentId();
        if(parentId != null) {
            boolean isOwner = userDao.isOwner(userId, parentId);
            if(!isOwner) {
                throw new BusinessException(ServiceError.Forbidden);
            }
            
            storeDao.insert(store);
        }
        else {
            storeDao.insert(store);
            
            UserStoreAccount ma = new UserStoreAccount();
            ma.setStoreId(store.getId());
            ma.setUserId(userId);
            ma.setStatus(AccountStatus.Active);
            storeDao.insert(ma);
        }
    }

    @Override
    public Store getStore(String id) {
        return storeDao.getStore(id);
    }

    @Override
    public StoreList listUserStores(String userId) {
        StoreList result = new StoreList();
        result.setItems(userDao.listUserTopStores(userId));
        return result;
    }

    @Override
    public StoreTree getBranchTree(String id) {
        return storeDao.getBranchTree(id);
    }

    @Override
    public StoreList getBranches(String id) {
        StoreList result = new StoreList();
        result.setItems(storeDao.getBranches(id));
        return result;
    }

    @Override
    public CouponList listCoupons(String id) {
        CouponList list = new CouponList();
        list.setItems(couponDao.query(Coupon.class, Collections.singletonMap("storeId", id)));
        return list;
    }

    @Override
    public StoreList listTopStores(String userId) {
        StoreList result = new StoreList();
        result.setItems(storeDao.listTopStores(userId));
        return result;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void createWalmartStores() throws IOException {
        String chain = "WALMART";
        Store parent = storeDao.getUniqueResult(Store.class, Collections.singletonMap("name", chain));
        if(parent == null) {
            parent = new Store();
            parent.setName(chain);
            storeDao.insert(parent);
        }
        
        InputStream stream = this.getClass().getResourceAsStream("/walmart-store-address.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while(true) {
            String line = br.readLine();
            if(line == null) {
                break;
            }
            String[] items = line.split(",");
            if(items.length<3)
                continue;
            String city = items[1].substring(1);
            String state = line.substring(line.length() - 8, line.length() - 6);
            String zip = line.substring(line.length() - 5);
            String[] split = items[0].split("\t");
            String storeid = split[0];
            String address = split[1];
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("parentId", parent.getId());
            params.put("chainStoreId", storeid);
            storeDao.delete(Store.class, params);
            Store store = new Store();
            store.setParentId(parent.getId());
            store.setChainStoreId(storeid);
            store.setAddress(address);
            store.setCity(city);
            store.setState(state);
            store.setZipcode(zip);
            storeDao.insert(store);
        }
        stream.close();
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void createTargetStores() throws IOException {
        createStores("TARGET", "/target-store-address.txt");
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void createCvsStores() throws IOException {
        createStores("CVS", "/cvs-store-address.txt");
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void createSafewayStores() throws IOException {
        createStores("SAFEWAY", "/safeway-store-address.txt");
    }
    
    private void createStores(String chain, String resource) throws IOException {
        Store parent = storeDao.getUniqueResult(Store.class, Collections.singletonMap("name", chain));
        if(parent == null) {
            parent = new Store();
            parent.setName(chain);
            storeDao.insert(parent);
        }
        
        InputStream stream = this.getClass().getResourceAsStream(resource);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while(true) {
            String line = br.readLine();
            if(line == null) {
                break;
            }
            String[] items = line.split("\t");
            if(items.length<6)
                continue;
            String city = items[4];
            String state = items[3];
            String zip = items[2];
            String storeid = items[0];
            String phone = items[1];
            String address = items[5];
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("parentId", parent.getId());
            params.put("chainStoreId", storeid);
            storeDao.delete(Store.class, params);
            Store store = new Store();
            store.setParentId(parent.getId());
            store.setChainStoreId(storeid);
            store.setAddress(address);
            store.setCity(city);
            store.setState(state);
            store.setZipcode(zip);
            store.setPhone(phone);
            storeDao.insert(store);
        }
        stream.close();
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public int geocodeOneStore() {
        Store store = storeDao.getOneStoreWithoutGeo();
        if(store == null)
            return 0;
        
        try {
            String addr = store.getAddress() + "," + store.getCity() + "," + store.getState();
            Geocoder geocoder = new Geocoder();
            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(addr).setLanguage("en").getGeocoderRequest();
            GeocodeResponse resp = geocoder.geocode(geocoderRequest);
            List<GeocoderResult> results = resp.getResults();
            if(results.size() == 0) {
                store.setLatitude(new BigDecimal(0));
                store.setLongitude(new BigDecimal(0));
            }
            else {
                BigDecimal lat = results.get(0).getGeometry().getLocation().getLat();
                store.setLatitude(lat);
                BigDecimal lng = results.get(0).getGeometry().getLocation().getLng();
                store.setLongitude(lng);
            }
            System.out.println("GEO for " + addr + " is " + store.getLatitude() + "," + store.getLongitude());
            storeDao.updateEntity(store, "latitude", "longitude");
        }
        catch (Exception e) {
            e.printStackTrace();
            store.setLatitude(null);
            store.setAddress2(null);
            storeDao.updateEntity(store, "latitude", "address2");
        }
        
        return 1;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void createShoplocalStores() throws IOException {
        String[] files = ("stores-v2.tsv.0" +
        		" stores-v2.tsv.1" +
        		" stores-v2.tsv.2" +
        		" stores-v2.tsv.3" +
        		" stores-v2.tsv.4" +
        		" stores-v2.tsv.5" +
        		" stores-v2.tsv.6").split(" ");
        for(String file : files) {
            String path = "../coupon-crawler/" + file;
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            while(true) {
                String line = br.readLine();
                if(line == null)
                    break;
                String[] fields = line.split("\t");
                try {
                    Store store = new Store();
                    store.setChainStoreId(getField(fields[0]));
                    store.setName(getField(fields[1]));
                    store.setAddress(getField(fields[2]));
                    store.setCity(getField(fields[3]));
                    store.setState(getField(fields[4]));
                    store.setZipcode(getField(fields[5]));
                    store.setWeb(getField(fields[6]));
                    store.setPhone(PhoneNumberUtil.formatPhone(getField(fields[7])));
                    storeDao.insert(store);
                }
                catch (Exception e) {
                    System.out.println(line);
                    e.printStackTrace();
                }
            }
        }
    }
    
    private String getField(String field) {
        if(field == null || field.trim().length() == 0 || "null".equalsIgnoreCase(field))
            return null;
        return field.trim();
    }
}
