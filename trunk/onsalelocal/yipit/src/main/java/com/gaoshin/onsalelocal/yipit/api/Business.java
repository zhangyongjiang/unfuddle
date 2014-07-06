package com.gaoshin.onsalelocal.yipit.api;

import java.util.ArrayList;
import java.util.List;

public class Business {
	private String id;
	private String name;
	private String url;
	private List<Location> locations = new ArrayList<Location>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
}
