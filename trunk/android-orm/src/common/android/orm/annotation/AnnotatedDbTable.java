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
import java.util.ArrayList;
import java.util.List;

import common.android.orm.core.DbTable;
import common.android.orm.reflection.AnnotatedFieldCallback;
import common.android.orm.reflection.ReflectionUtil;

public class AnnotatedDbTable<T> extends DbTable<T> {
	private Table tableAnno;

    public AnnotatedDbTable(Class<T> mappingClass) throws Exception {
        this(mappingClass, mappingClass.getAnnotation(Table.class));
    }

    public AnnotatedDbTable(Class<T> mappingClass, Table tableAnno) throws Exception {
        super(mappingClass);

        this.tableAnno = tableAnno;

        String tableName = tableAnno.name();
        if (tableName.equals(Table.DEFAULT)) {
            tableName = mappingClass.getSimpleName();
        }
        setTableName(tableName);

        ReflectionUtil.iterateAnnotatedFields(mappingClass, (Object) null, Column.class, new AnnotatedFieldCallback() {
            @Override
            public void field(Object o, Field field) throws Exception {
                addMappingColumn(new AnnotatedDbColumn(field));
            }
        });
    }

	public Table getTableAnnotation() {
		return tableAnno;
	}
	
	public List<Ddl> getDdls() {
		List<Ddl> all = new ArrayList<Ddl>();
		for(Ddl ddl : tableAnno.ddls()) {
			all.add(ddl);
		}
		return all;
	}
	
}
