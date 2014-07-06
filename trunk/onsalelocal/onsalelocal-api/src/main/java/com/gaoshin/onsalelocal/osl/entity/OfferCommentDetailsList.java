package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.api.OfferDetails;

@XmlRootElement
public class OfferCommentDetailsList {
	private OfferDetails offerDetails;
	private List<OfferCommentDetails> items = new ArrayList<OfferCommentDetails>();

	public List<OfferCommentDetails> getItems() {
		return items;
	}

	public void setItems(List<OfferCommentDetails> items) {
		this.items = items;
	}

	public OfferDetails getOfferDetails() {
	    return offerDetails;
    }

	public void setOfferDetails(OfferDetails offerDetails) {
	    this.offerDetails = offerDetails;
    }
}
