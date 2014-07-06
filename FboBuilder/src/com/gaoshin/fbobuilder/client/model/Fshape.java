package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Shape;

public class Fshape<T extends Shape> extends Fnode<T> {

	public Fshape(T node) {
	    super(node);
    }
}
