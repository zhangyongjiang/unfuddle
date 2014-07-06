/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.canvas;

import com.sencha.gxt.desktopapp.client.Presenter;
import com.sencha.gxt.desktopapp.client.persistence.FileModel;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;

public interface CanvasPresenter extends Presenter {

	boolean isEnableCreate();

	boolean isEnableDelete();

	boolean isEnableEditName();

	boolean isEnableOpen();

	void onCollapse();

	void onCreate(FileType fileType);

	void onDelete();

	void onEditFileNameComplete(boolean isSaved);

	void onEditName();

	void onExpand();

	void onOpen();

	void onSelect(FileModel fileModel);

	void onNewEllipse();

	void onNewLine();

	void onHelp();

	void onZoomin();

	void onZoomout();

	void onNewPloygon();

	void onDragEnd(String pos);

}