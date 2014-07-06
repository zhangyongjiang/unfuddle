package com.gaoshin.onsalelocal.api;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.osl.entity.User;

@XmlRootElement
public class OfferDetails extends Offer {
	private Float distance;
	private boolean bookmarked;
	private int stores;
	private User submitter;

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public boolean isBookmarked() {
		return bookmarked;
	}

	public void setBookmarked(boolean bookmarked) {
		this.bookmarked = bookmarked;
	}

	public int getStores() {
	    return stores;
    }

	public void setStores(int stores) {
	    this.stores = stores;
    }

	public User getSubmitter() {
	    return submitter;
    }

	public void setSubmitter(User poster) {
	    this.submitter = poster;
    }
}
