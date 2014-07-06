package com.gaoshin.coupon.android.model;

import java.util.List;
import java.util.UUID;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;

public class ConfigurationServiceImpl {
    private SORMA sorma;
    
    private String deviceId;

    public ConfigurationServiceImpl(SORMA sorma) {
        this.sorma = sorma;
        checkDeviceId();
    }

    private void checkDeviceId() {
        Configuration conf = get(ConfKey.DeviceId);
        if(conf == null) {
            deviceId = UUID.randomUUID().toString();
            conf = new Configuration();
            conf.setValue(deviceId);
            save(conf);
        }
        else {
            deviceId = conf.getValue();
        }
    }

    public Configuration get(String key) {
        String[] whereValues = new String[] {key};
        String where = "_key = ?";
        Configuration conf = sorma.get(Configuration.class, where, whereValues);
        return conf;
    }

    public Configuration get(String key, Object def) {
        Configuration conf = get(key);
        if(conf == null) {
            conf = new Configuration();
            conf.setKey(key);
            conf.setValue(def == null ? null : def.toString());
        }
        return conf;
    }

    public void save(Configuration conf) {
        if(conf.getId() == null) {
            conf.setId((int)sorma.insert(conf));
        }
        else {
            sorma.update(conf);
        }
    }

    public void delete(String key) {
        String[] whereValues = new String[] {key};
        String where = "_key = ?";
        sorma.delete(Configuration.class, where, whereValues);
    }

    public void delete(int id) {
        String[] whereValues = new String[] {id+""};
        String where = "_id = ?";
        sorma.delete(Configuration.class, where, whereValues);
    }

    public List<Configuration> getList(String key) {
        String[] whereValues = new String[] {key};
        String where = "_key = ?";
        return sorma.select(Configuration.class, where, whereValues);
    }

    public List<Configuration> getList(Enum key) {
        return getList(key.name());
    }

    public Configuration get(Enum key) {
        return get(key.name());
    }

    public Configuration get(Enum key, Object def) {
        return get(key.name(), def);
    }

    public void delete(Enum key) {
        delete(key.name());
    }
    
    public <T> T get(String key, Class<T> cls) {
        Configuration c = get(key);
        if(c == null) {
            return null;
        }
        return JsonUtil.toJavaObject(c.getValue(), cls);
    }
    
    public <T> T get(Enum key, Class<T> cls) {
        return get(key.name(), cls);
    }
    
    public <T> T get(Class<T> cls) {
        Configuration c = get(cls.getName());
        if(c == null) {
            return null;
        }
        return JsonUtil.toJavaObject(c.getValue(), cls);
    }
    
    public void save(Object obj) {
        String key = obj.getClass().getName();
        save(key, obj);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void save(Enum key, Object obj) {
        save(key.name(), obj);
    }

    public void save(String key, Object obj) {
        Configuration configuration = get(key);
        if(configuration == null) {
            configuration = new Configuration();
            configuration.setKey(key);
        }
        configuration.setValue(JsonUtil.toJsonString(obj));
        save(configuration);
    }

}
