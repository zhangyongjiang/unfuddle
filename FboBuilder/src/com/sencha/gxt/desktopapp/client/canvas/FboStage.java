package com.sencha.gxt.desktopapp.client.canvas;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Stage;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.editor.Adjustor;

public class FboStage {
	private Stage stage;
	private List<FboLayer> layers = new ArrayList<FboLayer>();
	private FboLayer currentLayer;
	private Adjustor adjustor;

	public FboStage(Stage stage) {
		this.setStage(stage);
    }

	public Stage getStage() {
	    return stage;
    }

	public void setStage(Stage stage) {
	    this.stage = stage;
    }

	public void draw() {
//
//		final Box2d box2d = new Box2d(80, 160, 300, 300);
//		rect = Kinetic.createRectangle(box2d);
//		rect.setCornerRadius(18);
//		rect.setStroke(Colour.darkcyan);
//		rect.setStrokeWidth(6);
//		rect.setRotation(-Math.PI / 4);
//		layer.add(rect);
//		stage.addEventListener(Event.Type.DRAGEND, new Node.EventListener() {
//			@Override
//			public boolean handle(Event evt) {
//				String pos = rect.getPosition().toString();
//				window.setHeadingHtml(pos);
//				canvasPresenter.onDragEnd(pos);
//				return true;
//			}
//		});
//		
		stage.draw();
    }
	
    public void onZoomout() {
		Vector2d scale = stage.getScale();
		scale.x = scale.x / 1.2;
		scale.y = scale.y / 1.2;
		stage.setScale(scale);
		adjustor.onScaleChanged();
		stage.draw();
    }

    public void onZoomin() {
		Vector2d scale = stage.getScale();
		scale.x = scale.x * 1.2;
		scale.y = scale.y * 1.2;
		stage.setScale(scale);
		adjustor.onScaleChanged();
		stage.draw();
    }
    
    public void newLayer() {
		FboLayer fboLayer = new FboLayer(this);
		Layer klayer = Kinetic.createLayer();
		fboLayer.setKlayer(klayer);
		stage.add(klayer);
		setCurrentLayer(fboLayer);
		
		adjustor = new Adjustor(klayer);
    }

	public void onNewLine() {
		createLayerIfNoLayer();
		currentLayer.onNewLine();
		stage.draw();
    }
	
	public FboLayer getCurrentLayer() {
	    return currentLayer;
    }

	public void setCurrentLayer(FboLayer currentLayer) {
	    this.currentLayer = currentLayer;
    }

	public List<FboLayer> getLayers() {
	    return layers;
    }

	public void setLayers(List<FboLayer> layers) {
	    this.layers = layers;
    }
	
	private FboLayer getTopLayer() {
		Node top = null;
		for(Node node : stage.getChildren()) {
			if(top == null) 
				top = node;
			else if(node.getZIndex() > top.getZIndex())
				top = node;
		}
		return (FboLayer) top;
	}
}
