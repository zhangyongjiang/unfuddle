package com.gaoshin.onsalelocal.osl.beans;

import java.util.ArrayList;
import java.util.List;

import com.gaoshin.onsalelocal.osl.entity.Bookmark;

public class BookmarkList {
	private List<Bookmark> items = new ArrayList<Bookmark>();

	public List<Bookmark> getItems() {
		return items;
	}

	public void setItems(List<Bookmark> items) {
		this.items = items;
	}
}
