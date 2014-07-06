package com.gaoshin.onsaleflyer.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PosterList {
	private List<Poster> items = new ArrayList<Poster>();

	public List<Poster> getItems() {
		return items;
	}

	public void setItems(List<Poster> items) {
		this.items = items;
	}
}
