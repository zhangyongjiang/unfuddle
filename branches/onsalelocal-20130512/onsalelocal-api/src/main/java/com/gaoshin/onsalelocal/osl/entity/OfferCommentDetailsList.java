package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OfferCommentDetailsList {
	private List<OfferCommentDetails> items = new ArrayList<OfferCommentDetails>();

	public List<OfferCommentDetails> getItems() {
		return items;
	}

	public void setItems(List<OfferCommentDetails> items) {
		this.items = items;
	}
}
