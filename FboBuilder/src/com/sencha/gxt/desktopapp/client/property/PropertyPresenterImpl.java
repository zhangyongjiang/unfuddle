/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.property;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.desktopapp.client.DesktopBus;

public class PropertyPresenterImpl implements PropertyPresenter {

	private DesktopBus desktopBus;

	private PropertyView propertyView;

	public PropertyPresenterImpl(DesktopBus desktopBus) {
		this.desktopBus = desktopBus;
	}

	public DesktopBus getDesktopBus() {
		return desktopBus;
	}

	@Override
	public void go(HasWidgets hasWidgets) {
		hasWidgets.add(getPropertyView().asWidget());
	}

	private PropertyView getPropertyView() {
		if (propertyView == null) {
			propertyView = new PropertyViewImpl(this);
		}
		return propertyView;
	}

	@Override
    public void showProperty(String string) {
		getPropertyView().showProperty(string);
    }


}
