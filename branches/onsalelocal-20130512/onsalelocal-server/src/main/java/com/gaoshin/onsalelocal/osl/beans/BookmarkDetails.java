package com.gaoshin.onsalelocal.osl.beans;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;

public class BookmarkDetails extends Bookmark {
	private Offer offer;

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
}
