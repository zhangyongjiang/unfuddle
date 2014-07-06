package com.gaoshin.onsalelocal.osl.entity;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.api.Store;

@XmlRootElement
public class FavouriteStoreDetails extends FavouriteStore {
	private Store store;
	private User user;
	private Boolean myFollowingStore;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Store getStore() {
	    return store;
    }

	public void setStore(Store store) {
	    this.store = store;
    }

	public Boolean getMyFollowingStore() {
	    return myFollowingStore;
    }

	public void setMyFollowingStore(Boolean myFollowingStore) {
	    this.myFollowingStore = myFollowingStore;
    }
}
