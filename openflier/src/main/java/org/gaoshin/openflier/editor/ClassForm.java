package org.gaoshin.openflier.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.gaoshin.openflier.util.FieldFoundCallback;
import org.gaoshin.openflier.util.ReflectionUtil;

public class ClassForm {
	private Class cls;
	private List<FormField> items = new ArrayList<FormField>();
	
	public ClassForm(Class cls) {
		this.cls = cls;
		try {
			ReflectionUtil.iterateFields(cls, null, new FieldFoundCallback() {
				@Override
				public void field(Object o, Field field) throws Exception {
					items.add(new FormField(field));
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<FormField> getItems() {
		return items;
	}
}
