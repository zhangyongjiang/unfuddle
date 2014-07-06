/**
 * Copyright (c) 2011 SORMA
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Author: sorma@gaoshin.com
 */
package common.android.orm;

import java.util.List;

import android.content.ContentValues;
import android.net.Uri;

import common.android.orm.annotation.AnnotatedORMDefinition;
import common.android.orm.core.DbTable;

public class AnnotatedORM extends ORM {
    private String contentProviderName;
	
    public AnnotatedORM(AnnotatedORMDefinition definition) {
        super(definition);
        contentProviderName = definition.getContentProviderClass().getName();
	}

    public String getContentProviderName() {
        return contentProviderName;
    }

    public Uri getTableContentUri(Class tableClass) {
        DbTable table = ormDefinition.getTable(tableClass);
        return Uri.parse("content://" + contentProviderName + "/" + table.getTableName());
    }

	public <T> List<T> getObjectList(Class<T>clazz, String where, String[] whereValues) {
        Uri uri = getTableContentUri(clazz);
        return super.getObjectList(uri, clazz, where, whereValues);
	}
	
	public Uri insert(Object obj) {
        DbTable table = ormDefinition.getTable(obj.getClass());
        Uri uri = getTableContentUri(obj.getClass());
        return contentResolver.insert(uri, table.getContentValues(obj));
	}
	
	public int update(Class clazz, ContentValues values, String selection,
			String[] selectionArgs) {
        Uri uri = getTableContentUri(clazz);
        return contentResolver.update(uri, values, selection, selectionArgs);
	}
}
