package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class Tag  {
	@Id
	@Column(length=64)
	private String name;
	
	@Column
	private int popularity;

	public String getName() {
	    return name;
    }

	public void setName(String name) {
	    this.name = name;
    }

	public int getPopularity() {
	    return popularity;
    }

	public void setPopularity(int popularity) {
	    this.popularity = popularity;
    }
}
