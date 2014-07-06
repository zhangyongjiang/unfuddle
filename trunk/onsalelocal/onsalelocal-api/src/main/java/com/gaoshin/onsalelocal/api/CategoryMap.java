package com.gaoshin.onsalelocal.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class CategoryMap extends DbEntity {
	@Column(nullable = false, length = 64)
	private String source;
	@Column(nullable = false, length = 64)
	private String srcCat;
	@Column(nullable = false, length = 64)
	private String oslCat;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSrcCat() {
		return srcCat;
	}

	public void setSrcCat(String srcCat) {
		this.srcCat = srcCat;
	}

	public String getOslCat() {
		return oslCat;
	}

	public void setOslCat(String oslCat) {
		this.oslCat = oslCat;
	}

}
