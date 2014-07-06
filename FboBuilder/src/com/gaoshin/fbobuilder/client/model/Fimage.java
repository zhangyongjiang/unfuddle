package com.gaoshin.fbobuilder.client.model;

import java.util.List;

import net.edzard.kinetic.Image;
import net.edzard.kinetic.Vector2d;

public class Fimage extends Fshape<Image> {
	public Fimage(Image node) {
	    super(node);
    }

	@Override
    public List<Vector2d> getAnchors() {
	    return null;
    }
}
