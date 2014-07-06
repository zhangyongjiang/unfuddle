package com.gaoshin.onsalelocal.osl.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.osl.beans.BookmarkDetails;
import com.gaoshin.onsalelocal.osl.dao.BookmarkDao;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;
import com.gaoshin.onsalelocal.osl.entity.BookmarkType;
import common.db.dao.ConfigDao;
import common.db.dao.impl.GenericDaoImpl;
import common.util.reflection.ReflectionUtil;

@Repository("bookmarkDao")
public class BookmarkDaoImpl extends GenericDaoImpl implements BookmarkDao {
	private static final Logger log = Logger.getLogger(BookmarkDaoImpl.class);
	
	@Autowired private ConfigDao configDao;

	@Override
	public List<Bookmark> list(String userId, BookmarkType type) {
		if(type == null)
			return query(Bookmark.class, Collections.singletonMap("userId", userId));
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("type", type.name());
		return query(Bookmark.class, param);
	}

	@Override
	public void removeBookmarkByOfferId(String userId, String offerId) {
		String sql = "delete from Bookmark where userId=? and offerId=?";
		update(sql, userId, offerId);
	}

	@Override
	public void removeAll(String userId) {
		String sql = "delete from Bookmark where userId=?";
		update(sql, userId);
	}

	@Override
	public List<BookmarkDetails> listBookmarkDetailsForUser(String userId, BookmarkType type) {
		List<Bookmark> bks = list(userId, type);
		if(bks.size()==0)
			return new ArrayList<BookmarkDetails>();
		List<String> offerIds = new ArrayList<String>();
		for(Bookmark bm : bks) {
			offerIds.add(bm.getOfferId());
		}
		String sql = "select * from Offer where id in (:offerIds)";
		List<Offer> offers = queryBySql(Offer.class, Collections.singletonMap("offerIds", offerIds), sql);
		Map<String, Offer> map = new HashMap<String, Offer>();
		for(Offer o : offers) {
			map.put(o.getId(), o);
		}
		List<BookmarkDetails> list = new ArrayList<BookmarkDetails>();
		for(Bookmark bm : bks) {
			BookmarkDetails details = ReflectionUtil.copy(BookmarkDetails.class, bm);
			list.add(details);
			details.setOffer(map.get(bm.getOfferId()));
		}
		
		for(int i=list.size()-1; i>=0; i--) {
			if(list.get(i).getOffer() == null)
				list.remove(i);
		}
		
		return list;
	}
}
