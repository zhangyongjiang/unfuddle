/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.property;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class PropertyViewImpl implements PropertyView, HideHandler {

	private static final String TITLE = "Property";

	private PropertyPresenter propertyPresenter;

	private Window window;
	private PropertyIconProvider propertyIconProvider;
	private PropertyToolBar propertyToolBar;

	private VerticalLayoutContainer verticalLayoutContainer;

	private HtmlLayoutContainer htmlContainer;

	private TextButton btn;

	public PropertyViewImpl(PropertyPresenter propertyPresenter) {
		this.propertyPresenter = propertyPresenter;
	}

	@Override
	public Widget asWidget() {
		return getWindow();
	}

	private IconProvider<FileModel> getPropertyIconProvider() {
		if (propertyIconProvider == null) {
			propertyIconProvider = new PropertyIconProvider();
		}
		return propertyIconProvider;
	}

	private PropertyPresenter getPropertyPresenter() {
		return propertyPresenter;
	}

	private PropertyToolBar getPropertyToolBar() {
		if (propertyToolBar == null) {
			propertyToolBar = new PropertyToolBar(getPropertyPresenter());
		}
		return propertyToolBar;
	}

	private String getTitle(FileModel fileModel) {
		return TITLE;
	}

	private Window getWindow() {
		if (window == null) {
			window = new Window();
			window.setHeadingText(getTitle(null));
			window.getHeader().setIcon(Images.getImageResources().folder());
			window.setMinimizable(true);
			window.setMaximizable(true);
			window.setClosable(false);
			window.setOnEsc(false);
			window.addHideHandler(this);
			window.setWidget(getVerticalLayoutContainer());
			
		}
		return window;
	}

	private Widget getContainer(String html) {
		btn = new TextButton(html);
		return btn;
	}

	@Override
	public void onHide(HideEvent event) {
	}

	private VerticalLayoutContainer getVerticalLayoutContainer() {
		if (verticalLayoutContainer == null) {
			verticalLayoutContainer = new VerticalLayoutContainer();
			verticalLayoutContainer.setBorders(false);
			verticalLayoutContainer.add(getPropertyToolBar(),
			        new VerticalLayoutData(1, -1));
			verticalLayoutContainer.add(getContainer("hello world"),
			        new VerticalLayoutData(1, 1));
			verticalLayoutContainer.setScrollMode(ScrollMode.AUTO);
		}
		return verticalLayoutContainer;
	}

	@Override
    public void showProperty(String string) {
		btn.setText(string);
		window.forceLayout();
    }

}
