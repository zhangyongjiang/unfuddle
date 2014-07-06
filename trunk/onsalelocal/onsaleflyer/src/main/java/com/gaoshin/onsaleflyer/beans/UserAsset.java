package com.gaoshin.onsaleflyer.beans;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserAsset {
	private PosterOwner posterOwner;
	private String assetName;

	public UserAsset() {
	}
	
	public UserAsset(PosterOwner up, String assetName) {
		this.setAssetName(assetName);
		this.setPosterOwner(up);
	}
	
	public String getPath() {
		return getPosterOwner().getPath() + File.separator + getAssetName();
	}

	public String getRealPath(String base) {
		return base + File.separator + getPath();
	}

	public PosterOwner getPosterOwner() {
		return posterOwner;
	}

	public void setPosterOwner(PosterOwner posterOwner) {
		this.posterOwner = posterOwner;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public static UserAsset fromPath(String ownerId, String path) {
		int pos = path.lastIndexOf(File.separator);
		String assetName = path.substring(pos + 1);
		PosterOwner up = PosterOwner.fromPath(ownerId, path.substring(0, pos));
		return new UserAsset(up, assetName);
	}
	
	public String getUrlPath() {
		return posterOwner.getUrlPath() + "/" + assetName;
	}
}
