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
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import common.android.orm.reflection.ReflectionUtil;

public class DbTable<T> {
	private Class<T> mappingClass;
	private List<DbColumn> columns = new ArrayList<DbColumn>();
	private String tableName;

    public DbTable(Class<T> mappingClass) {
        this.mappingClass = mappingClass;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addMappingColumn(DbColumn dbColumn) {
        columns.add(dbColumn);
    }

    public void addMappingColumn(String fieldName, String columnName) {
        try {
            Field field = ReflectionUtil.getField(mappingClass, fieldName);
            columns.add(new DbColumn(field, columnName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	public Class<?> getMappingClas() {
		return mappingClass;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public T getObjectFromRow(Cursor cursor) {
		try {
			T obj = mappingClass.newInstance();
			for(DbColumn col : columns) {
				col.getValueFromCursor(cursor, obj);
			}
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void putToDbContentValues(ContentValues cv, Object obj) {
		try {
			for(DbColumn col : columns) {
                col.putToDbContentValues(cv, obj);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ContentValues getContentValues(Object obj) {
		ContentValues cv = new ContentValues();
		putToDbContentValues(cv, obj);
		return cv;
	}
	
}
