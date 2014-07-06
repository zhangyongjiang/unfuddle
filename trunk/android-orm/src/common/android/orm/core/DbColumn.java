/**
 * Copyright (c) 2011 SORMA
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Author: sorma@gaoshin.com
 */
package common.android.orm.core;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import common.android.orm.reflection.ReflectionUtil;

public class DbColumn {
	protected Field field;
    protected String columnName;
	
    public DbColumn(Field field) {
        this.field = field;
        field.setAccessible(true);
    }

    public DbColumn(Field field, String columnName) {
        this(field);
        this.columnName = columnName;
    }

	public Field getField() {
		return field;
	}
	
    protected void setColumnName(String columnName) {
        this.columnName = columnName;
    }

	public String getColumnName() {
        return columnName;
	}
	
	public void getValueFromCursor(Cursor cursor, Object obj) throws Exception {
		int index = cursor.getColumnIndex(getColumnName());
		if(index == -1)
			return;
		String strValue = cursor.getString(index);
		Object fieldValue = ReflectionUtil.convert(strValue, field.getType());
		field.set(obj, fieldValue);
	}
	
	public void putToDbContentValues(ContentValues cv, Object obj) throws Exception {
		Class<?> type = field.getType();
		Object fieldValue = field.get(obj);
		
		if (type.equals(String.class)) {
			cv.put(getColumnName(), (String)fieldValue);
			return;
		}
		
		if (type.equals(Integer.class) || type.equals(int.class)) {
			cv.put(getColumnName(), (Integer)fieldValue);
			return;
		}
		
		if (type.equals(Float.class) || type.equals(float.class)) {
			cv.put(getColumnName(), (Float)fieldValue);
			return;
		}
		
		if (type.equals(Double.class) || type.equals(double.class)) {
			cv.put(getColumnName(), (Double)fieldValue);
			return;
		}
		
		if (type.equals(Long.class) || type.equals(long.class)) {
			cv.put(getColumnName(), (Long)fieldValue);
			return;
		}
		
		if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			cv.put(getColumnName(), (Boolean)fieldValue);
			return;
		}
		
		if (type.equals(Date.class)) {
			Long ts = (fieldValue == null) ? null : ((Date)fieldValue).getTime(); 
			cv.put(getColumnName(), ts);
			return;
		}
		
		if (type.equals(Calendar.class)) {
			Long ts = (fieldValue == null) ? null : ((Calendar)fieldValue).getTimeInMillis(); 
			cv.put(getColumnName(), ts);
			return;
		}
		
		if (type.isEnum()) {
			String value = (fieldValue == null) ? null : fieldValue.toString();
			cv.put(getColumnName(), (String)value);
			return;
		}
	}
}
