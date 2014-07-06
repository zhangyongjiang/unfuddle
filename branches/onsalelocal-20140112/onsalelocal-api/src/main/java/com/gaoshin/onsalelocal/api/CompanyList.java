package com.gaoshin.onsalelocal.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompanyList {
	private List<Company> items = new ArrayList<Company>();

	public List<Company> getItems() {
	    return items;
    }

	public void setItems(List<Company> items) {
	    this.items = items;
    }
}
