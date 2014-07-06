package com.gaoshin.fandroid;

import java.util.List;

import com.gaoshin.sorma.AnnotatedORM;

public class ConfigurationServiceImpl {
    private AnnotatedORM orm;

    public ConfigurationServiceImpl(AnnotatedORM orm) {
        this.orm = orm;
    }

    public Configuration get(String key) {
        String[] whereValues = new String[] {key};
        String where = "ckey = ?";
        Configuration conf = orm.getObject(Configuration.class, where, whereValues);
        return conf;
    }

    public Configuration get(String key, Object def) {
        Configuration conf = get(key);
        if(conf == null) {
            conf = new Configuration();
            conf.setKey(key);
            conf.setValue(def.toString());
        }
        return conf;
    }

    public void save(Configuration conf) {
        if(conf.getId() == null) {
            conf.setId((int)orm.insert(conf));
        }
        else
            orm.update(conf);
    }

    public void delete(String key) {
        String[] whereValues = new String[] {key};
        String where = "ckey = ?";
        orm.delete(Configuration.class, where, whereValues);
    }

    public void delete(int id) {
        String[] whereValues = new String[] {id+""};
        String where = "_id = ?";
        orm.delete(Configuration.class, where, whereValues);
    }

    public List<Configuration> getList(String key) {
        String[] whereValues = new String[] {key};
        String where = "ckey = ?";
        return orm.getObjectList(Configuration.class, where, whereValues);
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
}
