package com.gaoshin.onsalelocal.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class StoreSource extends DbEntity {
	@Column(nullable = true, length = 255) @Enumerated(EnumType.STRING)	private DataSource source;
	@Column(nullable = true, length = 255)	private String sourceId;
	@Column(nullable = true, length = 64)	private String storeId;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public DataSource getSource() {
	    return source;
    }

	public void setSource(DataSource source) {
	    this.source = source;
    }

	public String getStoreId() {
	    return storeId;
    }

	public void setStoreId(String storeId) {
	    this.storeId = storeId;
    }
}
