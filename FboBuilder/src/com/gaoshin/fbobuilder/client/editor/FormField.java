package com.gaoshin.fbobuilder.client.editor;

import java.lang.reflect.Field;

public class FormField {
	private String label;
	private Field field;

	public FormField(Field field) {
		this.field = field;
		label = field.getName();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public Field getField() {
		return field;
	}
}
