package com.sencha.gxt.desktopapp.client.canvas;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.canvas.images.Images;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class CanvasToolBar implements IsWidget {

	private static final String FILE_TYPE = "fileType";

	private CanvasPresenter canvasPresenter;

	private ToolBar toolBar;
	private TextButton createFolderTextButton;
	private TextButton btnHelp;
	private TextButton btnNewLine;
	private TextButton btnNewText;
	private TextButton createProgramTextButton;
	private TextButton btnNewEllipse;
	private TextButton btnNewCircle;
	private TextButton btnRemoveSelected;
	private TextButton zoomoutButton;
	private TextButton zoominButton;
	private SelectHandler helpHandler;
	private SelectHandler newLineHandler;
	private SelectHandler collapseSelectHandler;
	private SelectHandler deleteSelectHandler;

	private SelectHandler newEllipseHandler;

	private TextButton btnNewPloygon;

	private SelectHandler newTextHandler;

	private SelectHandler zoominHandler;

	private SelectHandler zoomoutHandler;

	private SelectHandler newPloygonHandler;

	public CanvasToolBar(CanvasPresenter canvasPresenter) {
		this.canvasPresenter = canvasPresenter;
	}

	public Widget asWidget() {
		return getToolBar();
	}

	public void setButtonEnabledState() {
		boolean isEnableCreate = getPresenter().isEnableCreate();
		boolean isEnableDelete = getPresenter().isEnableDelete();
		getNewLineButton().setEnabled(isEnableCreate);
		getNewTextButton().setEnabled(isEnableCreate);
		getNewImageButton().setEnabled(isEnableCreate);
		getNewCircleButton().setEnabled(isEnableCreate);
		getNewEllipseButton().setEnabled(isEnableCreate);
		getRemoveSelectedButton().setEnabled(isEnableDelete);
	}

	protected CanvasPresenter getPresenter() {
		return canvasPresenter;
	}

	private SelectHandler getNewCircleHandler() {
		if (collapseSelectHandler == null) {
			collapseSelectHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onCollapse();
				}
			};
		}
		return collapseSelectHandler;
	}

	private TextButton getNewCircleButton() {
		if (btnNewCircle == null) {
			btnNewCircle = new TextButton();
			btnNewCircle.setToolTip("New&nbsp;Circle");
			btnNewCircle.setIcon(Images.getImageResources().circle_32());
			btnNewCircle.addSelectHandler(getNewCircleHandler());
		}
		return btnNewCircle;
	}

	private TextButton getNewEllipseButton() {
		if (btnNewEllipse == null) {
			btnNewEllipse = new TextButton();
			btnNewEllipse.setToolTip("New&nbsp;Ellipse");
			btnNewEllipse.setIcon(Images.getImageResources().ellipse_32());
			btnNewEllipse.setData(FILE_TYPE, FileType.BOOKMARK);
			btnNewEllipse.addSelectHandler(getNewEllipseHandler());
		}
		return btnNewEllipse;
	}

	private TextButton getHelpButton() {
		if (btnHelp == null) {
			btnHelp = new TextButton();
			btnHelp.setToolTip("Help");
			btnHelp.setIcon(Images.getImageResources().help_32());
			btnHelp.setData(FILE_TYPE, FileType.DOCUMENT);
			btnHelp.addSelectHandler(getHelpHandler());
		}
		return btnHelp;
	}

	private TextButton getNewImageButton() {
		if (createProgramTextButton == null) {
			createProgramTextButton = new TextButton();
			createProgramTextButton.setToolTip("New&nbsp;Image");
			createProgramTextButton.setIcon(Images.getImageResources()
			        .picture_32());
			createProgramTextButton.setData(FILE_TYPE, FileType.PROGRAM);
			createProgramTextButton.addSelectHandler(getNewEllipseHandler());
		}
		return createProgramTextButton;
	}

	private TextButton getNewLineButton() {
		if (btnNewLine == null) {
			btnNewLine = new TextButton();
			btnNewLine.setToolTip("New&nbsp;Image");
			btnNewLine.setIcon(Images.getImageResources().line_32());
			btnNewLine.setData(FILE_TYPE, FileType.PROGRAM);
			btnNewLine.addSelectHandler(getNewLineHandler());
		}
		return btnNewLine;
	}

	private SelectHandler getNewEllipseHandler() {
		if (newEllipseHandler == null) {
			newEllipseHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onNewEllipse();
				}
			};
		}
		return newEllipseHandler;
	}

	private SelectHandler getNewLineHandler() {
		if (newLineHandler == null) {
			newLineHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onNewLine();
				}
			};
		}
		return newLineHandler;
	}

	private SelectHandler getHelpHandler() {
		if (helpHandler == null) {
			helpHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onHelp();
				}
			};
		}
		return helpHandler;
	}

	private TextButton getNewTextButton() {
		if (btnNewText == null) {
			btnNewText = new TextButton();
			btnNewText.setToolTip("New&nbsp;Text");
			btnNewText.setIcon(Images.getImageResources().text_icon_32());
			btnNewText.setData(FILE_TYPE, FileType.SPREADSHEET);
			btnNewText.addSelectHandler(getNewTextHandler());
		}
		return btnNewText;
	}

	private SelectHandler getNewTextHandler() {
		if (newTextHandler == null) {
			newTextHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onDelete();
				}
			};
		}
		return newTextHandler;
    }

	private SelectHandler getDeleteSelectHandler() {
		if (deleteSelectHandler == null) {
			deleteSelectHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onDelete();
				}
			};
		}
		return deleteSelectHandler;
	}

	private TextButton getRemoveSelectedButton() {
		if (btnRemoveSelected == null) {
			btnRemoveSelected = new TextButton();
			btnRemoveSelected.setToolTip("Remove&nbsp;Selected");
			btnRemoveSelected.setIcon(Images.getImageResources().remove_32());
			btnRemoveSelected.addSelectHandler(getDeleteSelectHandler());
		}
		return btnRemoveSelected;
	}

	private TextButton getZoominButton() {
		if (zoominButton == null) {
			zoominButton = new TextButton();
			zoominButton.setToolTip("Zoom&nbsp;In");
			zoominButton.setIcon(Images.getImageResources().zoomin_32());
			zoominButton.addSelectHandler(getZoominHandler());
		}
		return zoominButton;
	}

	private SelectHandler getZoominHandler() {
		if (zoominHandler == null) {
			zoominHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onZoomin();
				}
			};
		}
		return zoominHandler;
    }

	private SelectHandler getZoomoutHandler() {
		if (zoomoutHandler == null) {
			zoomoutHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onZoomout();
				}
			};
		}
		return zoomoutHandler;
    }

	private SelectHandler getNewPloygonHandler() {
		if (newPloygonHandler == null) {
			newPloygonHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					getPresenter().onNewPloygon();
				}
			};
		}
		return newPloygonHandler;
    }

	private TextButton getZoomoutButton() {
		if (zoomoutButton == null) {
			zoomoutButton = new TextButton();
			zoomoutButton.setToolTip("Zoom&nbsp;Out");
			zoomoutButton.setIcon(Images.getImageResources().zoomout_32());
			zoomoutButton.addSelectHandler(getZoomoutHandler());
		}
		return zoomoutButton;
	}

	private TextButton getNewPloygonButton() {
		if (btnNewPloygon == null) {
			btnNewPloygon = new TextButton();
			btnNewPloygon.setToolTip("New&nbsp;Polygon");
			btnNewPloygon.setIcon(Images.getImageResources().polygon3_32());
			btnNewPloygon.addSelectHandler(getNewPloygonHandler());
		}
		return btnNewPloygon;
	}

	private Widget getToolBar() {
		if (toolBar == null) {
			toolBar = new ToolBar();
			toolBar.setBorders(false);
			toolBar.add(getNewLineButton());
			toolBar.add(getNewTextButton());
			toolBar.add(getNewImageButton());
			toolBar.add(getNewCircleButton());
			toolBar.add(getNewEllipseButton());
			toolBar.add(getNewPloygonButton());
			toolBar.add(new SeparatorToolItem());
			toolBar.add(getZoominButton());
			toolBar.add(getZoomoutButton());
			toolBar.add(new SeparatorToolItem());
			toolBar.add(getRemoveSelectedButton());
		}
		return toolBar;
	}

}
