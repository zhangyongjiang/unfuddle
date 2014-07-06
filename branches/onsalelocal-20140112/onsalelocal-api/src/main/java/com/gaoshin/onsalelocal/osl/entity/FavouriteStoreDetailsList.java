package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FavouriteStoreDetailsList {
	private List<FavouriteStoreDetails> items = new ArrayList<FavouriteStoreDetails>();

	public List<FavouriteStoreDetails> getItems() {
		return items;
	}

	public void setItems(List<FavouriteStoreDetails> items) {
		this.items = items;
	}
}
