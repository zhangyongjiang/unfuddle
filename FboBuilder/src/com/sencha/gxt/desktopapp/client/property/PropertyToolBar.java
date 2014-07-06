package com.sencha.gxt.desktopapp.client.property;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class PropertyToolBar implements IsWidget {

	private static final String FILE_TYPE = "fileType";

	private PropertyPresenter propertyPresenter;

	private ToolBar toolBar;

	public PropertyToolBar(PropertyPresenter propertyPresenter) {
		this.propertyPresenter = propertyPresenter;
	}

	public Widget asWidget() {
		return getToolBar();
	}

	protected PropertyPresenter getPresenter() {
		return propertyPresenter;
	}

	private Widget getToolBar() {
		if (toolBar == null) {
			toolBar = new ToolBar();
			toolBar.setBorders(false);
		}
		return toolBar;
	}

}
