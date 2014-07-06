package com.gaoshin.onsaleflyer.beans;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PosterAsset {
	private PosterId posterId;
	private String assetName;

	public PosterAsset() {
	}
	
	public PosterAsset(String assetName, PosterId posterId) {
		this.assetName = assetName;
		this.posterId = posterId;
	}
	
	public PosterId getPosterId() {
		return posterId;
	}

	public void setPosterId(PosterId posterId) {
		this.posterId = posterId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	
	public String getPath() {
		return posterId.getPath() + File.separator + assetName;
	}
}
