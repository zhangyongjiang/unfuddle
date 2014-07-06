package com.sencha.gxt.desktopapp.client.canvas.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class Images {

	public interface ImageResources extends ClientBundle {
		ImageResource text_icon_32();
		ImageResource text_32();
		ImageResource circle_32();
		ImageResource line_32();
		ImageResource ellipse_32();
		ImageResource help_32();
		ImageResource exit_32();
		ImageResource image_32();
		ImageResource polygon_32();
		ImageResource picture_add_32();
		ImageResource polygon2_32();
		ImageResource polygon3_32();
		ImageResource remove_32();
		ImageResource zoomin_32();
		ImageResource zoomout_32();
		ImageResource picture_32();
	}

	private static ImageResources imageResources;

	public static ImageResources getImageResources() {
		if (imageResources == null) {
			imageResources = GWT.create(ImageResources.class);
		}
		return imageResources;
	}
}
