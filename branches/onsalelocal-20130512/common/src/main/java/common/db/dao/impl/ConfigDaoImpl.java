package common.db.dao.impl;

import java.util.Collections;

import org.springframework.stereotype.Repository;

import common.db.dao.ConfigDao;
import common.db.entity.Configuration;

@Repository("configDao")
public class ConfigDaoImpl extends GenericDaoImpl implements ConfigDao {

    @Override
    public String get(String name) {
        Configuration conf = getUniqueResult(Configuration.class, Collections.singletonMap("name", name));
        return conf == null ? null : conf.getValue();
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return Integer.parseInt(get(name, defaultValue));
    }

    @Override
    public long getLong(String name, long defaultValue) {
        return Long.parseLong(get(name, defaultValue));
    }

    @Override
    public float getFloat(String name, float defaultValue) {
        return Float.parseFloat(get(name, defaultValue));
    }

    @Override
    public void set(String name, Object value) {
        Configuration conf = getUniqueResult(Configuration.class, Collections.singletonMap("name", name));
        if(conf != null) {
            update(Configuration.class, Collections.singletonMap("value", value.toString()), Collections.singletonMap("name", name));
        }
        else {
            conf = new Configuration();
            conf.setName(name);
            conf.setValue(value.toString());
            insert(conf);
        }
    }

    @Override
    public String get(String name, Object defaultValue) {
        String value = get(name);
        return value == null ? defaultValue.toString() : value;
    }
}
