package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Ellipse;
import net.edzard.kinetic.Vector2d;

public class Fellipse extends Fshape<Ellipse> {

	public Fellipse(Ellipse node) {
	    super(node);
    }

	@Override
    public List<Vector2d> getAnchors() {
		List<Vector2d> list = new ArrayList<Vector2d>();
		Vector2d center = getNode().getPosition();
		Vector2d radius = getNode().getRadius();
		
		Vector2d hpos = new Vector2d();
		hpos.x = center.x + radius.x;
		hpos.y = center.y;
		list.add(hpos);
		
		Vector2d vpos = new Vector2d();
		vpos.x = center.x;
		vpos.y = center.y - radius.y;
		list.add(vpos);
		
	    return list;
    }

}
