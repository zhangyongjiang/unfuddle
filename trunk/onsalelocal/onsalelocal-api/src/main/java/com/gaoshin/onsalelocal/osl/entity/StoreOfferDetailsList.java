package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.api.OfferDetails;

@XmlRootElement
public class StoreOfferDetailsList {
	private StoreDetails storeDetails;
	private List<OfferDetails> items = new ArrayList<OfferDetails>();

	public StoreDetails getStoreDetails() {
		return storeDetails;
	}

	public void setStoreDetails(StoreDetails storeDetails) {
		this.storeDetails = storeDetails;
	}

	public List<OfferDetails> getItems() {
		return items;
	}

	public void setItems(List<OfferDetails> items) {
		this.items = items;
	}
}
