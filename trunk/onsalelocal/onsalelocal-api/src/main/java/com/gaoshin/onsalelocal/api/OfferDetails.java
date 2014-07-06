package com.gaoshin.onsalelocal.api;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.osl.entity.UserDetails;

@XmlRootElement
public class OfferDetails extends Offer {
	private Float distance;
	private boolean liked;
	private int stores;
	private UserDetails submitter;

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public int getStores() {
	    return stores;
    }

	public void setStores(int stores) {
	    this.stores = stores;
    }

	public UserDetails getSubmitter() {
	    return submitter;
    }

	public void setSubmitter(UserDetails poster) {
	    this.submitter = poster;
    }
}
