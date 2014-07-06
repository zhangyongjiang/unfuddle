package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY) 
@XmlType(propOrder="")
public class KeyValueList {
	private List<KeyValue> item;

	public void setItem(List<KeyValue> item) {
		this.item = item;
	}

	public List<KeyValue> getItem() {
		return item;
	}

	public List<KeyValue> getItemCreated() {
		if(item == null)
			item = new ArrayList<KeyValue>();
		return item;
	}

}
