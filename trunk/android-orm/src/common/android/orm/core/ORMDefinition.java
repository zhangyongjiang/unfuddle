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

import java.util.ArrayList;
import java.util.List;

public class ORMDefinition {
    protected List<DbTable> tables = new ArrayList<DbTable>();
	
    public void addTable(DbTable dbTable) {
        tables.add(dbTable);
    }

    public DbTable getTable(Class mappingClass) {
        for (DbTable table : tables) {
			if(table.getMappingClas().equals(mappingClass)) {
				return table;
			}
		}
		throw new RuntimeException("cannot find table for class " + mappingClass);
	}
	
    public DbTable getTableByName(String name) {
        for (DbTable table : tables) {
			if(table.getTableName().equals(name)) {
				return table;
			}
		}
		throw new RuntimeException("cannot find table " + name);
	}
}
