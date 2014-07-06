package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Group;

public abstract class Fgroup<T extends Group> extends Fcontainer<T> {

	public Fgroup(T node) {
	    super(node);
    }

}
