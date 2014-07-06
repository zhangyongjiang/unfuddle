package org.gaoshin.openflier.editor;

import java.lang.reflect.Field;

import org.gaoshin.openflier.util.CamelUnderScore;

public class FormField {
	private String label;
	private Field field;

	public FormField(Field field) {
		this.field = field;
		label = CamelUnderScore.camelToLabel(field.getName());
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
