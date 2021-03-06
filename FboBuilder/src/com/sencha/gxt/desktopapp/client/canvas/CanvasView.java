/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.canvas;

import com.google.gwt.user.client.ui.IsWidget;

public interface CanvasView extends IsWidget {

	void onZoomout();

	void onZoomin();

	void onNewLine();

}