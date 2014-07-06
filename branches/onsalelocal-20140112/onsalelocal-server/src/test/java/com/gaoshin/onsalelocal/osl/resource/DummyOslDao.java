package com.gaoshin.onsalelocal.osl.resource;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Sort;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.entity.Notification;
import com.gaoshin.onsalelocal.osl.entity.Tag;
import com.gaoshin.onsalelocal.osl.entity.User;
import common.db.dao.City;
import common.db.util.ClassTree;

public class DummyOslDao implements OslDao {

	@Override
    public void insert(Object obj) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void replace(Object obj) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void insert(Object obj, boolean ignore) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public <T> T getEntity(Class<T> cls, String id, String... columns) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> T getUniqueResult(Class<T> cls,
            Map<String, ? extends Object> where, String... columns) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> List<T> query(Class<T> cls, Map<String, ? extends Object> where,
            String... columns) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> T getUniqueResult(Class<T> cls,
            Map<String, ? extends Object> where) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> List<T> query(Class<T> cls, Map<String, ? extends Object> where) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public void updateEntity(Object obj, String... columns) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void delete(Class class1, Map values) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void update(Class<?> class1, Map<String, ? extends Object> values,
            Map<String, ? extends Object> where) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public <T> List<T> queryColumn(Class<?> cls,
            Map<String, ? extends Object> where, String column) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> List<T> queryBySql(Class<T> cls,
            Map<String, ? extends Object> params, String sql) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> T queryUniqueBySql(Class<T> cls,
            Map<String, ? extends Object> params, String sql) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Object getDetailsAndParents(ClassTree classTree, String id) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List getDetailsAndParents(ClassTree classTree, String sql,
            Map<String, ? extends Object> params) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Object getDetailsAndChildren(ClassTree classTree, String id) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List getDetailsAndChildren(ClassTree classTree, String sql,
            Map<String, ? extends Object> params) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public int update(String sql) {
	    // TODO Auto-generated method stub
	    return 0;
    }

	@Override
    public int update(String sql, Object... args) {
	    // TODO Auto-generated method stub
	    return 0;
    }

	@Override
    public int update(String sql, String[] names, Object[] values) {
	    // TODO Auto-generated method stub
	    return 0;
    }

	@Override
    public List<Offer> listOffersIn(float lat, float lng, int radius,
            int offset, int size, String source, Sort sort) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public int harvest(String schema) {
	    // TODO Auto-generated method stub
	    return 0;
    }

	@Override
    public void cleanup(int days) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void ondemandShoplocal(City city) {
	    // TODO Auto-generated method stub
	    
    }

    public List<Offer> listStoreOffers(String storeId) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<Tag> listTags() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<Offer> trend(float lat, float lng, int radius, int offset,
            int size) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> T queryUniqueBySql(Class<T> cls, Object[] params, String sql) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public JdbcTemplate getJdbcTemplate() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public StoreList nearbyStores(Float latitude, Float longitude,
            Float radius, boolean hasOffer) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Store searchStore(String source, String sourceId, Store store) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public <T> List<T> queryBySql(Class<T> cls, String sql, Object... objs) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<Offer> followingsDeals(String userId, int offset, int size) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<Notification> getLatestNotifications(String userId, int offset,
            int size) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Map<String, Store> getStoreMap(List<String> storeIds) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Map<String, Offer> getOfferMap(List<String> offerIds) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<Store> getStoreList(List<String> storeIds) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<Offer> getOfferList(List<String> offerIds) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<User> listFollowers(String userId) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<User> followingUsers(String userId) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Map<String, User> followingUserMap(String userId) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public void markAllNotificationsRead(String userId) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public int getTotalNotifications(String userId) {
	    // TODO Auto-generated method stub
	    return 0;
    }

	@Override
    public int getTotalUnreadNotifications(String userId) {
	    // TODO Auto-generated method stub
	    return 0;
    }

	@Override
    public List<Offer> listStoreOffers(String storeId, int offset, int size) {
	    // TODO Auto-generated method stub
	    return null;
    }

}
