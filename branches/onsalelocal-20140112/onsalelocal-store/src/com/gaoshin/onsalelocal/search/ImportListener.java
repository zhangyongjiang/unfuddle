package com.gaoshin.onsalelocal.search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.EventListener;

public abstract class ImportListener implements EventListener {
    private static final String PROP_FILE = "ImportListener.properties";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    
    private Map reqParams = null;
    
    @Override
    public void onEvent(Context ctx) {
        try {
            Map<String, Object> params = ctx.getRequestParameters();
            Field field = params.getClass().getDeclaredField("m");
            field.setAccessible(true);
            reqParams = (Map) field.get(params);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void setParam(Object key, Object value) {
        reqParams.put(key, value);
    }
    
    protected Object getParam(Object key) {
        return reqParams.get(key);
    }
    
    protected Properties getProperties(Context ctx){
        String file = ctx.getSolrCore().getDataDir() + "/../" + PROP_FILE;
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(file);
            props.load(fis);
            fis.close();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }
    
    protected void saveProperties(Context ctx, Properties props){
        try {
            String file = ctx.getSolrCore().getDataDir() + "/../" + PROP_FILE;
            FileOutputStream fos = new FileOutputStream(file);
            props.save(fos, null);
            fos.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void saveProperties(Context ctx, String key, Object value){
        System.out.println("save property " + key + "=" + value);
        Properties properties = getProperties(ctx);
        properties.put(key, value);
        saveProperties(ctx, properties);
    }
    
    protected int getSolrId(Context ctx) {
        String name = ctx.getSolrCore().getName();
        int pos = name.lastIndexOf("-");
        if(pos == -1)
            return 0;
        else
            return Integer.parseInt(name.substring(pos+1));
    }
    
    protected int getDbId(Context ctx) {
        return Integer.parseInt(ctx.getRequestParameters().get("dbid").toString());
    }
    
    protected String getDbKey(String key, int dbid) {
        return key + "." + dbid;
    }
}
