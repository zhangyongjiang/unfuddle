package com.gaoshin.onsalelocal.osl.dao.impl;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.osl.dao.FavouriteDao;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.User;
import common.db.dao.ConfigDao;
import common.db.dao.impl.GenericDaoImpl;

@Repository("favouriteDao")
public class FavouriteOfferDaoImpl extends GenericDaoImpl implements FavouriteDao {
	private static final Logger log = Logger.getLogger(FavouriteOfferDaoImpl.class);
	
	@Autowired private ConfigDao configDao;

	@Override
    public List<FavouriteOffer> listByUserId(String userId) {
	    return query(FavouriteOffer.class, Collections.singletonMap("userId", userId));
    }

	@Override
    public List<FavouriteOffer> listByOfferId(String offerId) {
	    return query(FavouriteOffer.class, Collections.singletonMap("offerId", offerId));
    }

	@Override
    public List<User> listLikedUsers(String offerId) {
	    String sql = "select u.* from User u, FavouriteOffer f where f.userId = u.id and f.offerId=:offerId";
		return queryBySql(User.class, Collections.singletonMap("offerId", offerId), sql );
    }

	@Override
    public List<Offer> listUserLikedOffers(String userId) {
	    String sql = "select o.* from Offer o, FavouriteOffer f where f.offerId = o.id and f.userId=:userId";
		return queryBySql(Offer.class, Collections.singletonMap("userId", userId), sql );
    }

	@Override
    public List<User> listLikedUsersForStore(String storeId) {
	    String sql = "select u.* from User u, FavouriteStore f where f.userId = u.id and f.storeId=:storeId";
		return queryBySql(User.class, Collections.singletonMap("storeId", storeId), sql );
    }

	@Override
    public List<Store> listUserLikedStores(String userId) {
	    String sql = "select s.* from Store s, FavouriteStore f where f.storeId = s.id and f.userId=:userId";
		return queryBySql(Store.class, Collections.singletonMap("userId", userId), sql );
    }

	@Override
    public List<User> listCommentedUsersForStore(String storeId) {
	    String sql = "select u.* from User u, StoreComment f where f.userId = u.id and f.storeId=:storeId";
		return queryBySql(User.class, Collections.singletonMap("storeId", storeId), sql );
    }

	@Override
    public List<User> listCommentedUsersForOffer(String offerId) {
	    String sql = "select u.* from User u, OfferComment f where f.userId = u.id and f.offerId=:offerId";
		return queryBySql(User.class, Collections.singletonMap("offerId", offerId), sql );
    }

	@Override
    public boolean isLiked(String userId, String offerId) {
		String sql = "select count(*) from FavouriteOffer where userId=? and offerId=?";
	    return getJdbcTemplate().queryForInt(sql, userId, offerId) > 0;
    }

}
