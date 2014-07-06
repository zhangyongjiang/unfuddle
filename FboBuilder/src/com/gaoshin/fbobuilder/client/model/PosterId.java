package com.gaoshin.fbobuilder.client.model;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PosterId {
	private String groupId;
	private String artifactId;
	private int version;
	
	public PosterId() {
	}
	
	public PosterId(String groupId, String artifactId, int version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getPath() {
		return groupId.replace('.', File.separatorChar) + File.separator + artifactId + File.separator + version;
	}

	public void setPath(String path) {
		String[] s = path.split(File.separator);
		version = Integer.parseInt(s[s.length - 1]);
		artifactId = s[s.length - 2];
		int pos = path.indexOf(artifactId);
		groupId = path.substring(0, pos).replaceAll(File.separator, ".");
	}

	public String getUrlPath() {
		return groupId + "/" + artifactId + "/" + version;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null)
			return false;
		PosterId p = (PosterId) arg0;
		return getUrlPath().equals(p.getUrlPath());
	}
}
