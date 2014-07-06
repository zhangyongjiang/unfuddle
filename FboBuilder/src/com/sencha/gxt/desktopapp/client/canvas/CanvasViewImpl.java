/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.canvas;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Stage;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.desktopapp.client.filemanager.images.Images;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class CanvasViewImpl implements CanvasView, HideHandler {
	private static final String TITLE = "Canvas";

	private CanvasPresenter canvasPresenter;

	private Window window;
	private CanvasIconProvider canvasIconProvider;
	private CanvasToolBar canvasToolBar;
	private CanvasMenu canvasMenu;

	private VerticalLayoutContainer verticalLayoutContainer;

	private FboStage fboStage;

	public CanvasViewImpl(CanvasPresenter canvasPresenter) {
		this.canvasPresenter = canvasPresenter;
	}

	@Override
	public Widget asWidget() {
		return getWindow();
	}

	private IconProvider<FileModel> getCanvasIconProvider() {
		if (canvasIconProvider == null) {
			canvasIconProvider = new CanvasIconProvider();
		}
		return canvasIconProvider;
	}

	private CanvasMenu getCanvasMenu() {
		if (canvasMenu == null) {
			canvasMenu = new CanvasMenu(getCanvasPresenter());
		}
		return canvasMenu;
	}

	private CanvasPresenter getCanvasPresenter() {
		return canvasPresenter;
	}

	private CanvasToolBar getCanvasToolBar() {
		if (canvasToolBar == null) {
			canvasToolBar = new CanvasToolBar(getCanvasPresenter());
			canvasToolBar.setButtonEnabledState();
		}
		return canvasToolBar;
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

	private Widget getContainer() {
		CenterLayoutContainer con = new CenterLayoutContainer();

		ContentPanel panel = new ContentPanel();
		panel.setBorders(false);
		panel.setHeaderVisible(false);
		panel.setWidth(500);
		panel.setHeight(400);
		con.add(panel);
		
		{
			Stage stage = Kinetic.createStage(panel.getBody(), 500, 400);
			fboStage = new FboStage(stage);
			fboStage.draw();
		}
		
		return con;
	}

	@Override
	public void onHide(HideEvent event) {
	}

	private VerticalLayoutContainer getVerticalLayoutContainer() {
		if (verticalLayoutContainer == null) {
			verticalLayoutContainer = new VerticalLayoutContainer();
			verticalLayoutContainer.setBorders(false);
			verticalLayoutContainer.add(getCanvasToolBar(),
			        new VerticalLayoutData(1, -1));
			verticalLayoutContainer.add(getContainer(),
			        new VerticalLayoutData(1, 1));
			verticalLayoutContainer.setScrollMode(ScrollMode.AUTO);
		}
		return verticalLayoutContainer;
	}

	@Override
    public void onZoomout() {
		fboStage.onZoomout();
    }

	@Override
    public void onZoomin() {
		fboStage.onZoomin();
    }

	@Override
    public void onNewLine() {
		fboStage.onNewLine();
    }

}
