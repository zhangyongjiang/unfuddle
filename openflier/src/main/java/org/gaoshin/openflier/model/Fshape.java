package org.gaoshin.openflier.model;

import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Shadow;
import net.edzard.kinetic.Shape.LineJoin;

public class Fshape extends Fnode {
	private Colour stroke;
	private double strokeWidth;
	private LineJoin lineJoin;
	private Shadow shadow;

	public Colour getStroke() {
		return stroke;
	}

	public void setStroke(Colour stroke) {
		this.stroke = stroke;
	}

	public double getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public LineJoin getLineJoin() {
		return lineJoin;
	}

	public void setLineJoin(LineJoin lineJoin) {
		this.lineJoin = lineJoin;
	}

	public Shadow getShadow() {
		return shadow;
	}

	public void setShadow(Shadow shadow) {
		this.shadow = shadow;
	}
}
