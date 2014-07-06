package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FavouriteOfferDetailsList {
	private List<FavouriteOfferDetails> items = new ArrayList<FavouriteOfferDetails>();

	public List<FavouriteOfferDetails> getItems() {
		return items;
	}

	public void setItems(List<FavouriteOfferDetails> items) {
		this.items = items;
	}
}
