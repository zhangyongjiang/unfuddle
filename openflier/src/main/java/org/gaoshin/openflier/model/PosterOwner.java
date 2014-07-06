package org.gaoshin.openflier.model;

import java.io.File;

public class PosterOwner {
	public static final String ContentFileName = "CONTENT";
	
	private PosterId posterId;
	private String ownerId;

	public PosterOwner() {
	}
	
	public PosterOwner(String ownerId, PosterId posterId) {
		this.ownerId = ownerId;
		this.setPosterId(posterId);
	}
	
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String userId) {
		this.ownerId = userId;
	}
	
	public String getRealPath(String base) {
		return base + File.separator + getPath();
	}
	
	public String getPath() {
		return getPath(this.ownerId) + File.separator + posterId.getPath();
	}

	public PosterId getPosterId() {
		return posterId;
	}

	public void setPosterId(PosterId posterId) {
		this.posterId = posterId;
	}
	
	public UserAsset getVisibilityAsset(Visibility visibility) {
		return new UserAsset(this, visibility.getFileName());
	}
	
	public UserAsset getContentAsset() {
		return new UserAsset(this, ContentFileName);
	}
	
	public static String getPath(String ownerId) {
		String p1 = ownerId.substring(0, 5);
		String p2 = ownerId.substring(5, 8);
		String p3 = ownerId.substring(8, 11);
		return p1 + File.separator + p2 + File.separator + p3 + File.separator + ownerId;
	}

	public static PosterOwner fromPath(String ownerId, String path) {
		int pos = path.indexOf(ownerId);
		String[] items = path.substring(pos).split(File.separator);
		int version = Integer.parseInt(items[items.length - 1]);
		String artifactId = items[items.length - 2];
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i<items.length - 2; i++) {
			if(sb.length() > 0)
				sb.append(".");
			sb.append(items[i]);
		}
		String groupId = sb.toString();
		return new PosterOwner(ownerId, new PosterId(groupId, artifactId, version));
	}

	public String getUrlPath() {
		return ownerId + "/" + posterId.getUrlPath();
	}
}
