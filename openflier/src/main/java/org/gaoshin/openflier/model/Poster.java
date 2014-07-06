package org.gaoshin.openflier.model;

import java.util.ArrayList;
import java.util.List;

public class Poster extends PosterOwner {
	private PosterOwner parent;
	private String title;
	private String description;
	private int width;
	private int height;
	private Visibility visibility = Visibility.Private;
	private List<Fshape> items = new ArrayList<Fshape>();

	public Poster() {
	}
	
	public Poster(String ownerId, PosterId posterId) {
		super(ownerId, posterId);
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

	public List<Fshape> getItems() {
		return items;
	}

	public void setItems(List<Fshape> items) {
		this.items = items;
	}
}
