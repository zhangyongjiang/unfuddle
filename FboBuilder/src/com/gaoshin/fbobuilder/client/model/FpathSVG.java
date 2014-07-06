package com.gaoshin.fbobuilder.client.model;

import java.util.List;

import net.edzard.kinetic.PathSVG;
import net.edzard.kinetic.Vector2d;

public class FpathSVG extends Fshape<PathSVG> {

	public FpathSVG(PathSVG node) {
	    super(node);
    }

	@Override
    public List<Vector2d> getAnchors() {
		throw new RuntimeException("unimplemented");
    }

}
