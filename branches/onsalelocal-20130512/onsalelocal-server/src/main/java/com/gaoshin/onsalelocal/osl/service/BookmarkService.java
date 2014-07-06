package com.gaoshin.onsalelocal.osl.service;

import java.util.List;

import com.gaoshin.onsalelocal.osl.beans.BookmarkDetails;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;
import com.gaoshin.onsalelocal.osl.entity.BookmarkType;


public interface BookmarkService {

	Bookmark add(Bookmark bookmark);

	List<Bookmark> list(String userId, BookmarkType type);

	void removeBookmarkByOfferId(String userId, String bookmarkId);

	void removeAll(String userId);

	List<BookmarkDetails> listDetails(String userId, BookmarkType type);

	void removeBookmarkById(String userId, String id);
}
