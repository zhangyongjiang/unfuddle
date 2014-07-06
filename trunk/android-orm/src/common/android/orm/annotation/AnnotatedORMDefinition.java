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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import common.android.orm.core.DbTable;
import common.android.orm.core.ORMDefinition;

public class AnnotatedORMDefinition extends ORMDefinition {
    private Class<? extends AbstractORMContentProvider> contentProviderClass;
    private int version;
    private ORMMapping ormMapping;
	
    public AnnotatedORMDefinition(Class<? extends AbstractORMContentProvider> ormcp) {
        this.contentProviderClass = ormcp;
        this.ormMapping = ormcp.getAnnotation(ORMMapping.class);
        version = getVersion(ormMapping.yyyyMMddHHmmss());
        for (Class<?> cls : ormMapping.ORMClasses()) {
            try {
                addTable(new AnnotatedDbTable(cls));
            } catch (Exception e) {
                throw new TableDefinitionException(e);
            }
        }
	}

    public Class<? extends AbstractORMContentProvider> getContentProviderClass() {
        return contentProviderClass;
    }

    private static int getVersion(String yyyyMMddHHmmss) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date timestamp = sdf.parse(yyyyMMddHHmmss);
            return (int) ((timestamp.getTime() - sdf.parse("20100101000000").getTime()) / 1000);
        } catch (ParseException e1) {
            throw new RuntimeException("wrong timestamp format defined " + yyyyMMddHHmmss);
        }

    }

    public String getDatabaseName() {
        return ormMapping.dbname();
    }

    public int getVersion() {
        return version;
    }

    public void createTables(SQLiteDatabase db) {
        for (DbTable table : tables) {
            String sql = ((AnnotatedDbTable) table).getTableAnnotation().create();
            db.execSQL(sql);
        }
    }

    public void upgradeTables(SQLiteDatabase db, int oldVersion, int newVersion) throws Exception {
        List<Ddl> all = new ArrayList<Ddl>();
        for (DbTable table : tables) {
            all.addAll(((AnnotatedDbTable) table).getDdls());
        }

        Collections.sort(all, new Comparator<Ddl>() {
            @Override
            public int compare(Ddl arg0, Ddl arg1) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    long ts0 = sdf.parse(arg0.yyyyMMddHHmmss()).getTime();
                    long ts1 = sdf.parse(arg1.yyyyMMddHHmmss()).getTime();
                    return (int) (ts0 - ts1);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
                );

        for (Ddl upgrade : all) {
            int ddlVersion = getVersion(upgrade.yyyyMMddHHmmss());
            if (ddlVersion > oldVersion && ddlVersion <= newVersion) {
                db.execSQL(upgrade.value());
            }
        }
    }
}
