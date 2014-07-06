package com.gaoshin.onsaleflyer.entity;

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
public class Flyer extends DbEntity {
	@Column(nullable = false, length = 64) private String userId;
    @Column(nullable=false, length=64) private String posterId;
    @Column(nullable=false, length=64) private String groupId;
    @Column(nullable=false, length=64) private String artifactId;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private Visibility scope;

	public String getPosterId() {
		return posterId;
	}

	public void setPosterId(String posterId) {
		this.posterId = posterId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Visibility getScope() {
		return scope;
	}

	public void setScope(Visibility scope) {
		this.scope = scope;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
}
