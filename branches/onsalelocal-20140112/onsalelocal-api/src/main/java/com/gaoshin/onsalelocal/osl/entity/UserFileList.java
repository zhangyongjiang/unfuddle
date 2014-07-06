package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserFileList  {
	private List<UserFile> items = new ArrayList<UserFile>();

	public List<UserFile> getItems() {
	    return items;
    }

	public void setItems(List<UserFile> items) {
	    this.items = items;
    }
}
