package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class Tag  {
	private String name;

	public String getName() {
	    return name;
    }

	public void setName(String name) {
	    this.name = name;
    }
}
