package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class UserFile extends DbEntity {
	@Column(nullable = true, length = 64)
	private String userId;

	@Column(nullable = true, length = 64)
	private String fileId;

	@Column(nullable = true)
	private Integer width;

	@Column(nullable = true)
	private Integer height;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileId() {
	    return fileId;
    }

	public void setFileId(String fileId) {
	    this.fileId = fileId;
    }

	public Integer getWidth() {
	    return width;
    }

	public void setWidth(Integer width) {
	    this.width = width;
    }

	public Integer getHeight() {
	    return height;
    }

	public void setHeight(Integer height) {
	    this.height = height;
    }

}
