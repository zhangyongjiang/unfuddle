package com.gaoshin.onsalelocal.api;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.osl.entity.UserDetails;

@XmlRootElement
public class UserOfferDetailsList extends OfferDetailsList {
	private UserDetails userDetails;

	public UserDetails getUserDetails() {
	    return userDetails;
    }

	public void setUserDetails(UserDetails userDetails) {
	    this.userDetails = userDetails;
    }
}
