package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Circle;
import net.edzard.kinetic.Vector2d;

public class Fcircle extends Fshape<Circle> {
	public Fcircle(Circle node) {
	    super(node);
    }

	@Override
	public List<Vector2d> getAnchors() {
		List<Vector2d> list = new ArrayList<Vector2d>();
		Vector2d center = getNode().getPosition();
		double radius = getNode().getRadius();
		Vector2d pos = new Vector2d();
		pos.x = center.x + radius;
		pos.y = center.y;
		list.add(pos);
	    return list;
    }
}
