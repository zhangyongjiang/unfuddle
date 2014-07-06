package org.gaoshin.openflier.model;

import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Text.FontStyle;
import net.edzard.kinetic.Text.HorizontalAlignment;

public class Ftext extends Fshape {
	private String text;
	private int fontSize;
	private String fontFamily;
	private FontStyle fontStyle;
	private Colour textFill;
	private Colour textStroke;
	private double textStrokeWidth;
	private HorizontalAlignment horizontalAlignment;
	private double padding;
	private double width;
	private int textHeight;
	private int textWidth;
	private double boxHeight;
	private int boxWidth;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}

	public Colour getTextFill() {
		return textFill;
	}

	public void setTextFill(Colour textFill) {
		this.textFill = textFill;
	}

	public Colour getTextStroke() {
		return textStroke;
	}

	public void setTextStroke(Colour textStroke) {
		this.textStroke = textStroke;
	}

	public double getTextStrokeWidth() {
		return textStrokeWidth;
	}

	public void setTextStrokeWidth(double textStrokeWidth) {
		this.textStrokeWidth = textStrokeWidth;
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public double getPadding() {
		return padding;
	}

	public void setPadding(double padding) {
		this.padding = padding;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public int getTextHeight() {
		return textHeight;
	}

	public void setTextHeight(int textHeight) {
		this.textHeight = textHeight;
	}

	public int getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(int textWidth) {
		this.textWidth = textWidth;
	}

	public double getBoxHeight() {
		return boxHeight;
	}

	public void setBoxHeight(double boxHeight) {
		this.boxHeight = boxHeight;
	}

	public int getBoxWidth() {
		return boxWidth;
	}

	public void setBoxWidth(int boxWidth) {
		this.boxWidth = boxWidth;
	}
}
