/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.canvas;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.desktopapp.client.DesktopBus;
import com.sencha.gxt.desktopapp.client.event.PropertyEvent;
import com.sencha.gxt.desktopapp.client.event.SelectFileModelEvent;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;

public class CanvasPresenterImpl implements CanvasPresenter {

	private DesktopBus desktopBus;

	private CanvasView canvasView;

	public CanvasPresenterImpl(DesktopBus desktopBus) {
		this.desktopBus = desktopBus;
	}

	public DesktopBus getDesktopBus() {
		return desktopBus;
	}

	@Override
	public void go(HasWidgets hasWidgets) {
		hasWidgets.add(getCanvasView().asWidget());
	}

	@Override
	public boolean isEnableCreate() {
		getCanvasView();
		return true;
	}

	@Override
	public boolean isEnableDelete() {
		return true;
	}

	@Override
	public boolean isEnableEditName() {
		return true;
	}

	@Override
	public boolean isEnableOpen() {
		return true;
	}

	@Override
	public void onCollapse() {
	}

	@Override
	public void onCreate(FileType fileType) {
	}

	@Override
	public void onDelete() {
	}

	@Override
	public void onEditFileNameComplete(boolean isSaved) {
	}

	@Override
	public void onEditName() {
	}

	@Override
	public void onExpand() {
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onSelect(FileModel fileModel) {
		getDesktopBus().fireSelectFileModelEvent(
				new SelectFileModelEvent(fileModel));
	}

	private CanvasView getCanvasView() {
		if (canvasView == null) {
			canvasView = new CanvasViewImpl(this);
		}
		return canvasView;
	}

	@Override
    public void onNewEllipse() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void onNewLine() {
		canvasView.onNewLine();
    }

	@Override
    public void onHelp() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void onZoomin() {
		canvasView.onZoomin();
    }

	@Override
    public void onZoomout() {
		canvasView.onZoomout();
    }

	@Override
    public void onNewPloygon() {
    }

	@Override
    public void onDragEnd(String str) {
	      getDesktopBus().firePropertyEvent(new PropertyEvent(str));
    }

}
