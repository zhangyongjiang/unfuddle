/**
 * Copyright (c) 2011 SORMA
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Author: sorma@gaoshin.com
 */
package common.android.orm.annotation;

import java.lang.reflect.Field;

import common.android.orm.core.DbColumn;

public class AnnotatedDbColumn extends DbColumn {
	protected Column annotation;
	
    public AnnotatedDbColumn(Field field) {
        super(field);
        this.annotation = field.getAnnotation(Column.class);

        String columnName = annotation.name();
        if (columnName.equals(Table.DEFAULT)) {
            columnName = field.getName();
        }
        setColumnName(columnName);
    }

	public boolean isKeyColumn() {
		return annotation.key();
	}
	
}
