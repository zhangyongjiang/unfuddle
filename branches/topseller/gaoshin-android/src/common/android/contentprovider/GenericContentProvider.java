package common.android.contentprovider;

import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class GenericContentProvider extends ContentProvider {
	public static final String PROVIDER_NAME = "common.android.contentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/configuration");

	public static final String _ID = "_id";
	public static final String KEY = "_key";
	public static final String VALUE = "_value";

	private static final int MSGS = 1;
	private static final int MSG_KEY = 2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "configuration", MSGS);
		uriMatcher.addURI(PROVIDER_NAME, "configuration/#", MSG_KEY);
	}

	// ---for database use---
	private SQLiteDatabase contentDb;
    private static final String DATABASE_NAME = "gaoshin_db";
	private static final String ConfigurationTable = "configuration";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table "
			+ ConfigurationTable + " (_id integer primary key autoincrement, "
			+ KEY + " text not null, " + VALUE + " text not null );";

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Content provider database",
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + ConfigurationTable);
			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case MSGS:
			count = contentDb.delete(ConfigurationTable, selection, selectionArgs);
			break;
		case MSG_KEY:
			String id = uri.getPathSegments().get(1);
			count = contentDb.delete(ConfigurationTable, _ID + " = " + id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		// ---get all messages---
		case MSGS:
			return "vnd.android.cursor.dir/vnd.bdcp.configuration ";
			// ---get a particular message---
		case MSG_KEY:
			return "vnd.android.cursor.item/vnd.bdcp.configuration ";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// ---add a new book---
		long rowID = contentDb.insert(ConfigurationTable, "", values);

		// ---if added successfully---
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		contentDb = dbHelper.getWritableDatabase();
		Log.i(GenericContentProvider.class.getName(), "create content provider for bdcp");
		
		if(contentDb != null) {
			initDbData();
		}
		
		return (contentDb == null) ? false : true;
	}
	
	private void initDbData() {
		HashSet<String> existing = new HashSet<String>();
		
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(ConfigurationTable);
		Cursor c = sqlBuilder.query(contentDb, null, null, null, null, null, null);
		if (c.moveToFirst()) {
			do {
				String dbkey = c.getString(c.getColumnIndex(GenericContentProvider.KEY));
				String value = c.getString(c.getColumnIndex(GenericContentProvider.VALUE));
				existing.add(dbkey + "---" + value);
			} while (c.moveToNext());
		}
		c.close();

		for(Configuration conf : DefaultSystemConfiguration.defaultConfigurations) {
			if(existing.contains(conf.getKey() + "---" + conf.getValue()))
				continue;
			ContentValues values = new ContentValues();
			values.put(KEY, conf.getKey());
			values.put(VALUE, conf.getValue());
			contentDb.insert(ConfigurationTable, "", values);
		}
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(ConfigurationTable);

		if (uriMatcher.match(uri) == MSG_KEY)
			sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));

		if (sortOrder == null || sortOrder == "")
			sortOrder = _ID;

		Cursor c = sqlBuilder.query(contentDb, projection, selection,
				selectionArgs, null, null, sortOrder);

		// ---register to watch a content URI for changes---
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case MSGS:
			count = contentDb.update(ConfigurationTable, values, selection,
					selectionArgs);
			break;
		case MSG_KEY:
			count = contentDb.update(
					ConfigurationTable,
					values,
					_ID
							+ " = "
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
