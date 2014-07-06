package com.gaoshin.fbobuilder.client.model;

import java.util.List;

import net.edzard.kinetic.Polygon;
import net.edzard.kinetic.Vector2d;

public class Fpolygon extends Fshape<Polygon> {

	public Fpolygon(Polygon node) {
	    super(node);
    }

	@Override
    public List<Vector2d> getAnchors() {
		return node.getPoints();
    }

}
