package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;

import com.gaoshin.onsalelocal.osl.beans.BookmarkDetails;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;
import com.gaoshin.onsalelocal.osl.entity.BookmarkType;
import common.db.dao.GenericDao;

public interface BookmarkDao extends GenericDao {

	List<Bookmark> list(String userId, BookmarkType type);

	void removeBookmarkByOfferId(String userId, String offerId);

	void removeAll(String userId);

	List<BookmarkDetails> listBookmarkDetailsForUser(String userId, BookmarkType type);
}
