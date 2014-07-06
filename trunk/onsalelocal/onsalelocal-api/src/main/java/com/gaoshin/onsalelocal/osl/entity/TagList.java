package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TagList  {
	private List<Tag> items = new ArrayList<Tag>();

	public List<Tag> getItems() {
	    return items;
    }

	public void setItems(List<Tag> items) {
	    this.items = items;
    }
}
