package com.gaoshin.fbobuilder.client.editor;

import java.util.ArrayList;
import java.util.List;

public class ClassForm {
	private Class cls;
	private List<FormField> items = new ArrayList<FormField>();
	
	public ClassForm(Class cls) {
		this.cls = cls;
//		try {
//			ReflectionUtil.iterateFields(cls, null, new FieldFoundCallback() {
//				@Override
//				public void field(Object o, Field field) throws Exception {
//					items.add(new FormField(field));
//				}
//			});
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}
	
	public List<FormField> getItems() {
		return items;
	}
}
