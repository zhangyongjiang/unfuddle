package com.gaoshin.onsalelocal.osl.entity;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.api.OfferDetails;

@XmlRootElement
public class FavouriteOfferDetails extends FavouriteOffer {
	private OfferDetails offer;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OfferDetails getOffer() {
	    return offer;
    }

	public void setOffer(OfferDetails offer) {
	    this.offer = offer;
    }
}
