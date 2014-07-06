package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Line;
import net.edzard.kinetic.Vector2d;


public class Fline extends Fshape<Line> {

	public Fline(Line node) {
	    super(node);
    }

	@Override
    public List<Vector2d> getAnchors() {
		List<Vector2d> list = new ArrayList<Vector2d>();
		Vector2d end = getNode().getEnd();
		list.add(end);
	    return list;
    }
}
