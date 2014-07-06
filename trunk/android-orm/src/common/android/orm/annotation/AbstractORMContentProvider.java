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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


public abstract class AbstractORMContentProvider extends ContentProvider {
	protected AnnotatedORMDefinition ormDefinition;
	protected SQLiteDatabase sqlitedb;
	
    public AbstractORMContentProvider() {
        Class<AbstractORMContentProvider> cls = (Class<AbstractORMContentProvider>) this.getClass();
        ormDefinition = new AnnotatedORMDefinition(cls);
	}
	
	@Override
	public boolean onCreate() {
		DatabaseHelper helper = new DatabaseHelper();
		sqlitedb = helper.getWritableDatabase();
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table = getTableNameFromUri(uri);
		return sqlitedb.delete(table, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		String table = getTableNameFromUri(uri);
        String type = "vnd.android.cursor.dir/vnd." + ormDefinition.getDatabaseName() + "." + table;
		return type;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = getTableNameFromUri(uri);
		long rowid = sqlitedb.insert(table, " ", values);
		return getContentUri(table, rowid);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String table = getTableNameFromUri(uri);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(table);
		Cursor cursor = qb.query(sqlitedb, projection, selection, selectionArgs, null, null, sortOrder);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String table = getTableNameFromUri(uri);
		return sqlitedb.update(table, values, selection, selectionArgs);
	}
	
	private String getTableNameFromUri(Uri uri) {
		String path = uri.toString();
		int pos = path.lastIndexOf('/');
		return path.substring(pos + 1);
	}

	private Uri getContentUri(String tableName) {
		return Uri.parse("content://" + getContentProviderName() + "/" + tableName);
	}
	
	private Uri getContentUri(String tableName, long id) {
		return ContentUris.withAppendedId(getContentUri(tableName), id);
	}
	
	public String getContentProviderName() {
        return this.getClass().getName();
	}

	public class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper() {
            super(getContext(), ormDefinition.getDatabaseName(), null, ormDefinition.getVersion());
		}
		
		@Override
		public void onOpen(SQLiteDatabase db){
			super.onOpen(db);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			ormDefinition.createTables(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				ormDefinition.upgradeTables(db, oldVersion, newVersion);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
