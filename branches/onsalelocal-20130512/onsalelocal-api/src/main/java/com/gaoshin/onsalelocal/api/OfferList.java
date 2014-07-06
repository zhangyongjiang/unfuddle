package com.gaoshin.onsalelocal.api;

import java.util.ArrayList;
import java.util.List;

public class OfferList {
	private List<Offer> items;

	public OfferList() {
		items = new ArrayList<Offer>();
    }
	
	public OfferList(List<Offer> items) {
		this.items = items;
    }
	
	public List<Offer> getItems() {
		return items;
	}

	public void setItems(List<Offer> items) {
		this.items = items;
	}
}
