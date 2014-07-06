package org.gaoshin.openflier.model;

import net.edzard.kinetic.Vector2d;

public class Fnode {
	private Vector2d position;
	private int zIndex;
	private double opacity;
	private Vector2d scale;
	private double rotation;

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public Vector2d getScale() {
		return scale;
	}

	public void setScale(Vector2d scale) {
		this.scale = scale;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
