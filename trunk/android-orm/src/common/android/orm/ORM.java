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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import common.android.orm.core.DbTable;
import common.android.orm.core.ORMDefinition;

public class ORM {
    protected ContentResolver contentResolver;
    protected ORMDefinition ormDefinition;
	
    public ORM(ORMDefinition definition) {
		this.ormDefinition = definition;
	}

    public ORMDefinition getORMDefinition() {
        return ormDefinition;
    }

    public ORM setContentResolver(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
		return this;
	}
	
    public <T> List<T> getObjectList(Uri uri, Class<T> clazz, String where, String[] whereValues) {
		Cursor cursor = null;
		List<T> result = new ArrayList<T>();
		try {
            DbTable<T> table = ormDefinition.getTable(clazz);
            cursor = contentResolver.query(uri, null, where, whereValues, null);
			if(cursor != null) {
				while(cursor.moveToNext()) {
                    result.add((T) table.getObjectFromRow(cursor));
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
	}
	
    public Uri insert(Uri uri, Object obj) {
        DbTable table = ormDefinition.getTable(obj.getClass());
        return contentResolver.insert(uri, table.getContentValues(obj));
	}
	
    public <T> T getObjectFromRow(Cursor cursor, Class<T> clazz) {
        return (T) ormDefinition.getTable(clazz).getObjectFromRow(cursor);
    }
}
