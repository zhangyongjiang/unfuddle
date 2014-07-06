package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StoreDetailsList {
	private List<StoreDetails> items = new ArrayList<StoreDetails>();

	public List<StoreDetails> getItems() {
		return items;
	}

	public void setItems(List<StoreDetails> items) {
		this.items = items;
	}
}
