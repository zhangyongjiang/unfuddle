package com.gaoshin.fbobuilder.client.editor;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Circle;
import net.edzard.kinetic.Group;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Line;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.model.Fnode;

public class Adjustor {
	private Fnode target;
	private Line vline;
	private Line hline;
	private Group container;
	private List<Circle> anchors = new ArrayList<Circle>();
	private Layer layer;
	
	public Adjustor(Layer layer) {
		this.layer = layer;
		container = Kinetic.createGroup();
		layer.add(container);
		container.setZIndex(Zindex.TOP);
		createHline();
		createVline();
    }
	
	public void hide() {
		container.hide();
	}
	
	public void show() {
		container.show();
	}
	
	private void createHline() {
		Vector2d start = new Vector2d(0, 0);
		Vector2d end = new Vector2d(0, 0);
		hline = Kinetic.createLine(start, end);
		container.add(hline);
	}
	
	private void createVline() {
		Vector2d start = new Vector2d(0, 0);
		Vector2d end = new Vector2d(0, 0);
		vline = Kinetic.createLine(start, end);
		container.add(vline);
	}
	
	public Fnode getTarget() {
	    return target;
    }
	
	public void setTarget(Fnode newTarget) {
		if(target != null) {
			for(Circle circle : anchors) {
				container.remove(circle);
			}
		}
		anchors.clear();
		
	    this.target = newTarget;
	    
		List<Vector2d> anchorPositions = target.getAnchors();
		for(Vector2d v : anchorPositions) {
			Circle circle = Kinetic.createCircle(v, getAnchorRadius());
			container.add(circle);
			anchors.add(circle);
		}
		
		Vector2d center = target.getPosition();
		container.move(center);
		
		double rotation = target.getNode().getRotation();
		container.setRotation(rotation);
    }

	private double getAnchorRadius() {
		return 5;
	}
	
	public void update() {
		
	}

	public void onScaleChanged() {
    }

}
