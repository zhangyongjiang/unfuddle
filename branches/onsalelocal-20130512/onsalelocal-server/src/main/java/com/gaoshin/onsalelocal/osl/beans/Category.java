package com.gaoshin.onsalelocal.osl.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Category {
    private String id;
    private String parentId;
    private String name;
    private int offerCount;
    private List<Category> items = new ArrayList<Category>();

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

    public List<Category> getItems() {
        return items;
    }

    public void setItems(List<Category> items) {
        this.items = items;
    }

    public Category search(String id) {
        for(Category cat : items) {
            if(cat.getId().equals(id))
                return cat;
        }
        return null;
    }

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getOfferCount() {
	    return offerCount;
    }

	public void setOfferCount(int offerCount) {
	    this.offerCount = offerCount;
    }
}
