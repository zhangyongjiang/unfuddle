package com.gaoshin.onsaleflyer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class AssetUsage extends DbEntity {
	@Column(nullable = false, length = 64)
	private String assetId;

	@Column(nullable = false, length = 64)
	private String posterItemId;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getPosterItemId() {
		return posterItemId;
	}

	public void setPosterItemId(String posterItemId) {
		this.posterItemId = posterItemId;
	}

}
