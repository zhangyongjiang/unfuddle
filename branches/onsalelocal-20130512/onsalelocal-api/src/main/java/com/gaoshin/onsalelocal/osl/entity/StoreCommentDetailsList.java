package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StoreCommentDetailsList {
	private List<StoreCommentDetails> items = new ArrayList<StoreCommentDetails>();

	public List<StoreCommentDetails> getItems() {
		return items;
	}

	public void setItems(List<StoreCommentDetails> items) {
		this.items = items;
	}
}
