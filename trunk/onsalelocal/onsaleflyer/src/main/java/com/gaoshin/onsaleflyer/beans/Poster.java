package com.gaoshin.onsaleflyer.beans;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsaleflyer.entity.Visibility;

@XmlRootElement
public class Poster {
	private PosterOwner owner;
	private PosterOwner parent;
	private String title;
	private String description;
	private int width;
	private int height;
	private Visibility visibility = Visibility.Private;

	public Poster() {
	}
	
	public Poster(String ownerId, PosterId posterId) {
		owner = new PosterOwner(ownerId, posterId);
	}
	
	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PosterOwner getParent() {
		return parent;
	}

	public void setParent(PosterOwner parent) {
		this.parent = parent;
	}

	public PosterOwner getOwner() {
		return owner;
	}

	public void setOwner(PosterOwner owner) {
		this.owner = owner;
	}
}
