package common.android.contentprovider;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import common.android.ConfKeyList;
import common.util.web.JsonUtil;

public class ContentProviderApi {
	/**
	 * Insert a configuration to content provider.
	 * @param contentResolver Usually from Actitity.getContentResolver.
	 * @param key
	 * @param value
	 * @return rowId will be returned.
	 */
	public static int insert(ContentResolver contentResolver,
			String key, String value) {
		Configuration conf = new Configuration(key, value);
		insert(contentResolver, conf);
		return conf.getId();
	}

	/**
	 * Create a new configuration.
	 * @param contentResolver  Usually from Actitity.getContentResolver.
	 * @param conf key/value will be saved into content provider
	 */
	public static void insert(ContentResolver contentResolver,
			Configuration conf) {
		ContentValues values = new ContentValues();
		values.put(GenericContentProvider.KEY, conf.getKey());
		values.put(GenericContentProvider.VALUE, conf.getValue());
		Uri uri = contentResolver.insert(
				GenericContentProvider.CONTENT_URI, values);
		int pos = uri.toString().lastIndexOf('/');
		conf.setId(Integer.parseInt(uri.toString().substring(pos + 1)));
	}

	/**
	 * Update configuration by key.
	 * Before call this method, make sure there is only one value for the key.
	 * @param contentResolver Usually from Actitity.getContentResolver.
	 * @param key 
	 * @param value the new value
	 * @throws MultipleRecordsException 
	 */
	public static void update(ContentResolver contentResolver,
			String key, String value) throws MultipleRecordsException {
		Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration != null)
            update(contentResolver, configuration.getId(), key, value);
        else
            insert(contentResolver, key, value);
	}

	/**
	 * Update by row id
	 * @param contentResolver
	 * @param id
	 * @param key
	 * @param value
	 */
	public static void update(ContentResolver contentResolver,
			int id, String key, String value) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(GenericContentProvider.KEY, key);
		editedValues.put(GenericContentProvider.VALUE, value);

		contentResolver.update(Uri.parse("content://common.android.contentprovider/configuration/" + id),
						editedValues, null, null);
	}

	/**
	 * Delete a configuration by a row id
	 * @param contentResolver
	 * @param id the row id for each configuration record
	 */
	public static void delete(ContentResolver contentResolver, int id) {
		contentResolver.delete(Uri.parse("content://common.android.contentprovider/configuration/" + id), null, null);
	}

	/**
	 * Delete configuration by a key
	 * @param contentResolver Usually from Actitity.getContentResolver.
	 * @param key 
	 * @throws MultipleRecordsException 
	 */
	public static void delete(ContentResolver contentResolver, String key) throws MultipleRecordsException {
		Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration != null) {
            contentResolver.delete(Uri.parse("content://common.android.contentprovider/configuration/" + configuration.getId()), null, null);
        }
	}

	/**
	 * delete all configurations
	 * @param contentResolver Usually from Actitity.getContentResolver.
	 */
	public static void deleteAll(ContentResolver contentResolver) {
		contentResolver.delete(Uri.parse("content://common.android.contentprovider/configuration"), null, null);
	}

    /**
     * Get configuration by row id
     * 
     * @param contentResolver
     * @param id
     *            the row id for a particular configuration record
     * @return Configuration instance. null if now row found
     */
	public static Configuration getConfiguration(ContentResolver contentResolver, int id) {
		Uri allTitles = Uri.parse("content://common.android.contentprovider/configuration/" + id);
		Cursor c = contentResolver.query(allTitles, null, null, null, null);
		Configuration conf = null;
		if (c.moveToFirst()) {
			do {
				String dbkey = c.getString(c.getColumnIndex(GenericContentProvider.KEY));
				String value = c.getString(c.getColumnIndex(GenericContentProvider.VALUE));
				conf = new Configuration();
				conf.setId(id);
				conf.setKey(dbkey);
				conf.setValue(value);
				break;
			} while (c.moveToNext());
		}
		c.close();
		
		return conf;
	}
	
	/**
	 * Get configuration by key. 
	 * @param contentResolver
	 * @param key
	 * @return
	 * @throws MultipleRecordsException 
	 */
	public static Configuration getConfiguration(ContentResolver contentResolver, String key) throws MultipleRecordsException {
		List<Configuration> list = list(contentResolver, key);
		if(list.size() == 0)
			return null;
		else if(list.size() > 1)
			throw new MultipleRecordsException("more than one record found for key " + key);
		else
			return list.get(0);
	}
	
	/**
	 * Get configuration value by key. 
	 * @param contentResolver
	 * @param key
	 * @return
	 * @throws MultipleRecordsException 
	 */
    public static String getStringConf(ContentResolver contentResolver, String key) throws MultipleRecordsException {
        Configuration conf = getConfiguration(contentResolver, key);
        return conf == null ? null : conf.getValue();
    }

    public static String getStringConf(ContentResolver contentResolver, String key, String value)
            throws MultipleRecordsException {
        Configuration conf = getConfiguration(contentResolver, key);
        if (conf == null || conf.getValue() == null)
            return value;
        else
            return conf.getValue();
    }

    /**
     * Get an integer configuration
     * 
     * @param contentResolver
     * @param key
     * @param defValue
     *            the default value
     * @return the int value of the configuration. If not found, the default
     *         value is returned
     * @throws MultipleRecordsException
     */
    public static int getIntConf(ContentResolver contentResolver, String key, int defValue)
            throws MultipleRecordsException {
        Configuration conf = getConfiguration(contentResolver, key);
        try {
            if (conf == null)
                return defValue;
            else
                return Integer.parseInt(conf.getValue());
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Get a Long configuration
     * 
     * @param contentResolver
     * @param key
     * @param defValue
     *            the default value
     * @return the long value of the configuration. If not found, the default
     *         value is returned
     * @throws MultipleRecordsException
     */
    public static long getLongConf(ContentResolver contentResolver, String key, long defValue)
            throws MultipleRecordsException {
        Configuration conf = getConfiguration(contentResolver, key);
        try {
            if (conf == null)
                return defValue;
            else
                return Long.parseLong(conf.getValue());
        } catch (Exception e) {
            return defValue;
        }
    }

    public static float getFloatConf(ContentResolver contentResolver, String key, float defValue)
            throws MultipleRecordsException {
        Configuration conf = getConfiguration(contentResolver, key);
        try {
            if (conf == null)
                return defValue;
            else
                return Float.parseFloat(conf.getValue());
        } catch (Exception e) {
            return defValue;
        }
    }

    public static double getDoubleConf(ContentResolver contentResolver, String key, double defValue)
            throws MultipleRecordsException {
        Configuration conf = getConfiguration(contentResolver, key);
        try {
            if (conf == null)
                return defValue;
            else
                return Double.parseDouble(conf.getValue());
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * Get a boolean configuration. Any value like 1, trUe, yeS will be treated as true, false otherwise.
	 * @param contentResolver
	 * @param key
	 * @param defValue the default value
	 * @return the boolean value of the configuration. If not found, the default value is returned
	 * @throws MultipleRecordsException
	 */
	public static boolean getBooleanConf(ContentResolver contentResolver, String key, boolean defValue) throws MultipleRecordsException {
		Configuration conf = getConfiguration(contentResolver, key);
		try {
			if(conf == null)
				return defValue;
			else
				return conf.getValue().equals("1") || conf.getValue().toLowerCase().startsWith("y") || conf.getValue().toLowerCase().startsWith("t") ;
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * find configurations by key. usually for key with multiple values 
	 * @param contentResolver
	 * @param key
	 * @return list of configurations
	 */
	public static List<Configuration> list(ContentResolver contentResolver, String key) {
		List<Configuration> list = new ArrayList<Configuration>();
		Uri allTitles = Uri
				.parse("content://common.android.contentprovider/configuration");
		Cursor c = null;
		if(key == null)
			c = contentResolver.query(allTitles, null, null, null, GenericContentProvider._ID + " asc");
		else
			c = contentResolver.query(allTitles, null, GenericContentProvider.KEY+"=?", new String[]{key},
				GenericContentProvider._ID + " asc");
		if (c.moveToFirst()) {
			do {
				int id = c.getInt(c.getColumnIndex(GenericContentProvider._ID));
				String dbkey = c.getString(c.getColumnIndex(GenericContentProvider.KEY));
				String value = c.getString(c.getColumnIndex(GenericContentProvider.VALUE));
				Configuration conf = new Configuration();
				conf.setId(id);
				conf.setKey(dbkey);
				conf.setValue(value);
				list.add(conf);
			} while (c.moveToNext());
		}
		c.close();
		
		return list;
	}
	
	public static void log(ContentResolver contentResolver, Throwable t) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter writer = new PrintWriter(sw);
			t.printStackTrace(writer);
			insert(contentResolver, ConfKeyList.Error.name(), sw.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static <T> T getConf(ContentResolver contentResolver, Class<T> clazz) throws MultipleRecordsException {
        String json = getStringConf(contentResolver, clazz.getName());
        if (json == null)
            return null;
        return JsonUtil.toJavaObject(json, clazz);
    }

    public static <T> List<T> getObjectList(ContentResolver contentResolver, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        List<Configuration> confs = list(contentResolver, clazz.getName());
        for (Configuration conf : confs) {
            list.add(JsonUtil.toJavaObject(conf.getValue(), clazz));
        }
        return list;
    }

    public static List<Configuration> getConfList(ContentResolver contentResolver, Class<?> clazz) {
        List<Configuration> confs = list(contentResolver, clazz.getName());
        return confs;
    }

    public static void save(ContentResolver contentResolver, Object obj) {
        String json = JsonUtil.toJsonString(obj);
        update(contentResolver, obj.getClass().getName(), json);
    }

    public static void append(ContentResolver contentResolver, Object obj) {
        String json = JsonUtil.toJsonString(obj);
        insert(contentResolver, obj.getClass().getName(), json);
    }
}
