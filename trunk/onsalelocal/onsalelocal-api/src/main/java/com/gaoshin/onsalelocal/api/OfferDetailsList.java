package com.gaoshin.onsalelocal.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OfferDetailsList {
	private List<OfferDetails> items = new ArrayList<OfferDetails>();

	public List<OfferDetails> getItems() {
		return items;
	}

	public void setItems(List<OfferDetails> items) {
		this.items = items;
	}
}
