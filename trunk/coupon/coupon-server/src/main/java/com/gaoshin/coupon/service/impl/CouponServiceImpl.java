package com.gaoshin.coupon.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.coupon.bean.City;
import com.gaoshin.coupon.bean.Client;
import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.bean.WebPage;
import com.gaoshin.coupon.crawler.CouponCategory;
import com.gaoshin.coupon.crawler.WebCoupon;
import com.gaoshin.coupon.crawler.WeeklyAd;
import com.gaoshin.coupon.crawler.shoplocal.ShoplocalCrawler;
import com.gaoshin.coupon.crawler.target.TargetWeeklyAdCrawler;
import com.gaoshin.coupon.crawler.walmart.WalmartCouponCrawler;
import com.gaoshin.coupon.dao.CategoryDao;
import com.gaoshin.coupon.dao.CouponDao;
import com.gaoshin.coupon.dao.GeoDao;
import com.gaoshin.coupon.dao.StoreDao;
import com.gaoshin.coupon.dao.UserDao;
import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.CouponStatus;
import com.gaoshin.coupon.entity.CrawlHistory;
import com.gaoshin.coupon.entity.FetchStatus;
import com.gaoshin.coupon.entity.ShoplocalCrawlHistory;
import com.gaoshin.coupon.entity.ShoplocalResult;
import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.service.CouponService;
import common.geo.Geocode;
import common.geo.PostalAddress;
import common.geo.PostalAddressParser;
import common.util.JacksonUtil;
import common.util.MD5;
import common.util.reflection.ReflectionUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class CouponServiceImpl extends ServiceBaseImpl implements CouponService {
    @Autowired private UserDao userDao;
    @Autowired private StoreDao storeDao;
    @Autowired private CouponDao couponDao;
    @Autowired private GeoDao geoDao;
    @Autowired private CategoryDao catDao;
    private boolean stopShoplocal = true;
    private Map<String, String> addr2Store = new HashMap<String, String>();

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void createCoupon(String userId, Coupon coupon) {
        String storeId = coupon.getStoreId();
        boolean isOwner = userDao.isOwner(userId, storeId);
        if(!isOwner)
            throw new BusinessException(ServiceError.Forbidden, "user doesn't own the store");
        
        coupon.setCreateByUserId(userId);
        coupon.setStatus(CouponStatus.Inactive);
        storeDao.insert(coupon);
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void enableCoupon(String userId, String id) {
        Coupon coupon = storeDao.getEntity(Coupon.class, id);
        if(CouponStatus.Active.equals(coupon.getStatus()))
            return;
        
        String storeId = coupon.getStoreId();
        boolean isOwner = userDao.isOwner(userId, storeId);
        if(!isOwner)
            throw new BusinessException(ServiceError.Forbidden, "user doesn't own the store");
        
        coupon.setStatus(CouponStatus.Active);
        storeDao.updateEntity(coupon, "status");
        
        StoreTree branchTree = storeDao.getBranchTree(storeId, "id", "parentId");
        createCouponTree(id, coupon, branchTree);
    }

    private void createCouponTree(String parentId, Coupon coupon, StoreTree branchTree) {
        for(StoreTree child : branchTree.getChildren()) {
            Coupon childCoupon = ReflectionUtil.copy(Coupon.class, coupon);
            childCoupon.setId(null);
            childCoupon.setParentId(parentId);
            childCoupon.setStoreId(child.getId());
            storeDao.insert(childCoupon);
            createCouponTree(parentId, coupon, child);
        }
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void disableCoupon(String userId, String id) {
        Coupon coupon = storeDao.getEntity(Coupon.class, id);
        if(CouponStatus.Inactive.equals(coupon.getStatus()))
            return;
        
        String storeId = coupon.getStoreId();
        boolean isOwner = userDao.isOwner(userId, storeId);
        if(!isOwner)
            throw new BusinessException(ServiceError.Forbidden, "user doesn't own the store");
        
        coupon.setStatus(CouponStatus.Inactive);
        storeDao.updateEntity(coupon, "status");
        storeDao.update(Coupon.class, Collections.singletonMap("status", CouponStatus.Inactive.name()), Collections.singletonMap("parentId", id));
    }

    @Override
    public void removeCoupon(String userId, String id) {
        Coupon coupon = storeDao.getEntity(Coupon.class, id);
        String storeId = coupon.getStoreId();
        boolean isOwner = userDao.isOwner(userId, storeId);
        if(!isOwner)
            throw new BusinessException(ServiceError.Forbidden, "user doesn't own the store");
        couponDao.removeCoupon(id);
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public CouponList crawl(Float lat, Float lng) throws Exception {
        CouponList cl = new CouponList();
        float radius = 20.0f;
        List<Store> stores = new ArrayList<Store>();
        List<Store> wstores = storeDao.nearbyCharinStores(lat, lng, radius, new String[]{"WALMART"});
        if(wstores.size() > 4)
            wstores = wstores.subList(0, 4);
        stores.addAll(wstores);
        List<Store> tstores = storeDao.nearbyCharinStores(lat, lng, radius, new String[]{"TARGET"});
        if(tstores.size() > 4)
            tstores = tstores.subList(0, 4);
        stores.addAll(tstores);
        
        String sql = "select * from Store where name=:name and parentId is null";
        Store targetStore = storeDao.queryUniqueBySql(Store.class, Collections.singletonMap("name", "TARGET"), sql);
        Store walmartStore = storeDao.queryUniqueBySql(Store.class, Collections.singletonMap("name", "WALMART"), sql);
        long now = System.currentTimeMillis();
        for(Store store : stores) {
            if(walmartStore.getId().equals(store.getParentId())) {
                CrawlHistory record = getLatestCrawlRecord(store.getId());
                if(record != null) {
                    long howlong = now - record.getUpdated();
                    if(howlong < 12 * 3600000)
                        continue;
                }
                
                List<CouponCategory> coupons = WalmartCouponCrawler.listStoreCoupons(store.getChainStoreId());
                for(CouponCategory cc : coupons) {
                    for(com.gaoshin.coupon.crawler.WebCoupon c : cc.getCoupons()) {
                        Coupon coupon = ReflectionUtil.copy(Coupon.class, c);
                        coupon.setTitle(c.getTitle());
                        coupon.setDescription(c.getDescription());
                        coupon.setCategory(cc.getName());
                        coupon.setListPrice(c.getListPrice());
                        coupon.setOriginalPrice(c.getOriginalPrice());
                        coupon.setValidDate(c.getValidDate());
                        coupon.setUpdated(now);
                        coupon.setCreated(now);
                        coupon.setImageUrl(c.getImageUrl());
                        coupon.setStatus(CouponStatus.Active);
                        coupon.setStoreId(store.getId());
                        coupon.setStoreName(store.getName());
                        coupon.setLatitude(store.getLatitude());
                        coupon.setLongitude(store.getLongitude());
                        couponDao.insert(coupon, true);
                    }
                }
                createCrawlRecord(store.getId());
            }
            
            if(targetStore.getId().equals(store.getParentId())) {
                CrawlHistory record = getLatestCrawlRecord(store.getId());
                if(record != null) {
                    long howlong = now - record.getUpdated();
                    if(howlong < 12 * 3600000)
                        continue;
                }
                String zip = store.getZipcode();
                if(zip == null)
                    continue;
                WeeklyAd weeklyAds = TargetWeeklyAdCrawler.getWeeklyAds(zip);
                for(CouponCategory cc : weeklyAds.getCategories()) {
                    for(com.gaoshin.coupon.crawler.WebCoupon c : cc.getCoupons()) {
                        Coupon coupon = ReflectionUtil.copy(Coupon.class, c);
                        coupon.setTitle(c.getTitle());
                        coupon.setDescription(c.getDescription());
                        coupon.setCategory(cc.getName());
                        coupon.setListPrice(c.getListPrice());
                        coupon.setOriginalPrice(c.getOriginalPrice());
                        coupon.setValidDate(c.getValidDate());
                        coupon.setUpdated(now);
                        coupon.setCreated(now);
                        coupon.setImageUrl(c.getImageUrl());
                        coupon.setStatus(CouponStatus.Active);
                        coupon.setStoreId(store.getId());
                        coupon.setStoreName(store.getName());
                        coupon.setLatitude(store.getLatitude());
                        coupon.setLongitude(store.getLongitude());
                        couponDao.insert(coupon, true);
                    }
                }
                createCrawlRecord(store.getId());
            }
        }

        List<Coupon> coupons = couponDao.searchCoupon(lat, lng, radius, null, null, 0, 50);
        cl.setItems(coupons);
        return cl;
    }

    private CrawlHistory getLatestCrawlRecord(String storeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        String sql = "select * from CrawlHistory where storeId=:storeId order by updated desc limit 1";
        CrawlHistory his = storeDao.queryUniqueBySql(CrawlHistory.class, params, sql);
        return his;
    }

    private CrawlHistory createCrawlRecord(String storeId) {
        CrawlHistory his = new CrawlHistory();
        his.setStoreId(storeId);
        storeDao.insert(his);
        return his;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public CouponList crawl(String zipcode) throws Exception {
        Geocode geocode = geoDao.getZipcodeLocation(zipcode);
        return crawl(geocode.getLatitude(), geocode.getLongitude());
    }

    @Override
    public CouponList searchByGeo(Float lat, Float lng, Float radius, String category, String keywords, int offset, int size) throws Exception {
        if(category != null && category.trim().length() == 0)
            category = null;
        if(keywords != null && keywords.trim().length() == 0)
            keywords = null;
        CouponList cl = new CouponList();
        List<Coupon> coupons = couponDao.searchCoupon(lat, lng, radius, category, keywords, offset, size);
        cl.setItems(coupons);
        return cl;
    }

    @Override
    public void stopShoplocal() {
        stopShoplocal = true;
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public ShoplocalCrawlHistory getOneShoplocalTask(Client client) throws Exception {
        String clientId = client.getId();
        Float lat = client.getLatitude();
        Float lng = client.getLongitude();
        Float radius = 20f;
        List<City> cities = geoDao.nearbyCities(lat , lng, radius);
        for(City city : cities) {
            ShoplocalCrawlHistory rec = couponDao.getOneShoplocalTask(city.getCity(), city.getState());
            if(rec != null)
                return rec;
        }
        ShoplocalCrawlHistory rec = couponDao.getOneShoplocalTask(null, null);
        return rec;
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public int crawlShoplocal() throws Exception {
        if(true)
            throw new RuntimeException("fix the nulls in couponDao.getOneShoplocalTask(null, null, null) ");
        int howmany = 0;
        stopShoplocal = false;
        ShoplocalCrawler crawler = new ShoplocalCrawler();
        while(!stopShoplocal) {
            ShoplocalCrawlHistory record = couponDao.getOneShoplocalTask(null, null);
            if(record == null)
                break;
    
            howmany ++;
            String loc = record.getCity() + "," + record.getState();
            crawler.changeLocation(loc);
            String url = record.getUrl();
            try {
                List<com.gaoshin.coupon.crawler.WebCoupon> coupons = crawler.getCoupons(url);
                System.out.println("total coupons found: " + coupons.size() + " for " + JacksonUtil.obj2Json(record));
                for(com.gaoshin.coupon.crawler.WebCoupon c : coupons) {
                    c.setCategory(record.getCategory());
                }
                insertWebCoupon(coupons);
                record.setStatus(FetchStatus.Fetched);
                couponDao.update(ShoplocalCrawlHistory.class, Collections.singletonMap("status", FetchStatus.Fetched.name()), Collections.singletonMap("id", record.getId()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        crawler.shutdown();
        
        return howmany;
    }

    private void insertWebCoupon(List<WebCoupon> coupons) {
        for(com.gaoshin.coupon.crawler.WebCoupon c : coupons) {
            if(c.getAddress() == null || c.getCity() == null || c.getState() == null) {
                continue;
            }
            PostalAddress postalAddress = PostalAddressParser.parse(c.getAddress(), c.getCity(), c.getState());
            c.setAddress(postalAddress.toString());
            c.setCity(postalAddress.getCity());
            c.setState(postalAddress.getState());
            
            String storeId = null;
            synchronized (this) {
                storeId = getStoreId(c.getAddress(), c.getCity(), c.getState(), c.getCompany());
            }
            if(storeId == null) {
                Store store = new Store();
                store.setAddress(c.getAddress());
                store.setCity(c.getCity());
                store.setState(c.getState());
                store.setName(c.getCompany());
                store.setPhone(c.getPhone());
                store.setZipcode(c.getZipcode());
                storeDao.insert(store);
                synchronized (this) {
                    storeId = store.getId();
                    setStoreId(c.getAddress(), c.getCity(), c.getState(), storeId);
                }
            }
            
            Coupon coupon = ReflectionUtil.copy(Coupon.class, c);
            String id = MD5.md5(storeId + c.getTitle() + c.getListPrice());
            coupon.setId(id);
            coupon.setStoreName(c.getCompany());
            coupon.setStatus(CouponStatus.Active);
            if(c.getStartDate() != null)
                coupon.setValidDate(c.getStartDate() + "-" + c.getEndDate());
            coupon.setCategory(c.getCategory());
            coupon.setStoreId(storeId);
            
            couponDao.replace(coupon);
        }
    }

    private String getStoreId(String address, String city, String state, String merchantName) {
        String key = address + city + state;
        String store = addr2Store.get(key);
        if(store == null) {
            Store s = storeDao.searchStore(address, city, state, merchantName);
            if(s != null) {
                addr2Store.put(key, s.getId());
                store = s.getId();
            }
        }
        return store;
    }

    private void setStoreId(String address, String city, String state, String storeId) {
        String key = address + city + state;
        addr2Store.put(key, storeId);
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Coupon getDetails(String couponId) throws Exception {
        Coupon coupon = couponDao.getEntity(Coupon.class, couponId, "*");
        if("SL".equals(coupon.getSource()) && coupon.getDescription() == null && coupon.getUrl() != null) {
            com.gaoshin.coupon.crawler.WebCoupon slCoupon = ShoplocalCrawler.getDetails(coupon.getUrl());
            if(slCoupon.getDescription() != null) {
                coupon.setDescription(slCoupon.getDescription());
                couponDao.updateEntity(coupon, "description");
            }
        }
        return coupon;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void uploadShoplocalResult(WebPage page) throws Exception {
        System.out.println("uploadShoplocalResult " + page.getId());
        ShoplocalCrawlHistory record = couponDao.getEntity(ShoplocalCrawlHistory.class, page.getId(), "id");
        
        record.setStatus(FetchStatus.HtmlReady);
        record.setUpdated(System.currentTimeMillis());
        couponDao.updateEntity(record, "fetched", "status", "updated");
        
        ShoplocalResult result = new ShoplocalResult();
        result.setHtml(page.getContent());
        result.setTaskId(record.getId());
        result.setPage(record.getPage());
        couponDao.insert(result);
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public int processShoplocalHtml() throws Exception {
        ShoplocalCrawlHistory record = couponDao.getOneShoplocalResult();
        if(record == null)
            return 0;
        
        ShoplocalResult result = couponDao.getShoplocalResult(record.getId(), record.getPage());
        try {
            ShoplocalCrawler crawler = new ShoplocalCrawler();
            List<com.gaoshin.coupon.crawler.WebCoupon> coupons = crawler.getCouponsFromHtml(result.getHtml());
            System.out.println("total coupons found: " + coupons.size() + " for " + JacksonUtil.obj2Json(record));
            for(com.gaoshin.coupon.crawler.WebCoupon c : coupons) {
                c.setCategory(record.getCategory());
            }
            
            if(coupons.size() == 0) {
                record.setStatus(FetchStatus.Fetched);
                record.setUpdated(System.currentTimeMillis());
                couponDao.updateEntity(record, "fetched", "status", "updated");
            }
            else {
                insertWebCoupon(coupons);
                record.setPage(record.getPage() + 1);
                record.setStatus(null);
                record.setUpdated(System.currentTimeMillis());
                couponDao.updateEntity(record, "fetched", "status", "page", "updated");
            }
        }
        catch (Exception e) {
            System.err.println("cannot process shoplocal task " + record.getId() + ". " + e.getMessage());
            record.setStatus(FetchStatus.Failed);
            record.setUpdated(System.currentTimeMillis());
            couponDao.updateEntity(record, "status", "updated");
        }
        
        return 1;
    }

    @Override
    public List<ShoplocalCrawlHistory> listShoplocalHistory(String status, int offset, int size) {
        return couponDao.listShoplocalHistory(status, offset, size);
    }

    @Override
    public List<ShoplocalResult> getShoplocalResults(String taskid) {
        return couponDao.getShoplocalResults(taskid);
    }

    @Override
    public ShoplocalResult getShoplocalResult(String taskid, int page) {
        return couponDao.getShoplocalResult(taskid, page);
    }

    @Override
    public List<ShoplocalResult> listShoplocalResults(String taskid) {
        return couponDao.getShoplocalResults(taskid);
    }
}
