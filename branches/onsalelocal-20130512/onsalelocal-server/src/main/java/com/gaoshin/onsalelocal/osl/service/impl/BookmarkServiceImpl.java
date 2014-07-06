package com.gaoshin.onsalelocal.osl.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.osl.beans.BookmarkDetails;
import com.gaoshin.onsalelocal.osl.dao.BookmarkDao;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;
import com.gaoshin.onsalelocal.osl.entity.BookmarkType;
import com.gaoshin.onsalelocal.osl.service.BookmarkService;

@Service
@Transactional(readOnly=true)
public class BookmarkServiceImpl extends ServiceBaseImpl implements BookmarkService {
    @Autowired private BookmarkDao bookmarkDao;
    @Autowired private OslDao offerDao;

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Bookmark add(Bookmark bookmark) {
		bookmarkDao.insert(bookmark, true);
		return bookmark;
	}

	@Override
	public List<Bookmark> list(String userId, BookmarkType type) {
		return bookmarkDao.list(userId, type);
	}

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void removeBookmarkByOfferId(String userId, String offerId) {
		bookmarkDao.removeBookmarkByOfferId(userId, offerId);
	}

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void removeAll(String userId) {
		bookmarkDao.removeAll(userId);
	}

	@Override
	public List<BookmarkDetails> listDetails(String userId, BookmarkType type) {
		return bookmarkDao.listBookmarkDetailsForUser(userId, type);
	}

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void removeBookmarkById(String userId, String id) {
		bookmarkDao.delete(Bookmark.class, Collections.singletonMap("id", id));
	}
}
