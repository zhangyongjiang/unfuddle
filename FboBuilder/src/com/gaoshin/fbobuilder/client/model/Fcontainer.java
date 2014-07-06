package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Container;

public abstract class Fcontainer<T extends Container> extends Fnode<T> {

	public Fcontainer(T node) {
	    super(node);
    }

}
