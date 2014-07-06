package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Star;
import net.edzard.kinetic.Vector2d;

public class Fstar extends Fshape<Star> {

	public Fstar(Star node) {
	    super(node);
    }

	@Override
    public List<Vector2d> getAnchors() {
		List<Vector2d> list = new ArrayList<Vector2d>();
	    Box2d bounds = node.getDragBounds();
	    Vector2d topleft = new Vector2d(bounds.left, bounds.top);
	    Vector2d rightbottom = new Vector2d(bounds.left, bounds.top);
	    list.add(topleft);
	    list.add(rightbottom);
	    return list;
    }

}
