package com.gaoshin.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Configuration;
import com.gaoshin.dao.ConfigurationDao;
import com.gaoshin.entity.ConfigurationEntity;
import common.util.JacksonUtil;

@Service("configurationService")
@Transactional
public class ConfigurationServiceImpl extends BaseServiceImpl implements ConfigurationService {
    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public Configuration save(Configuration conf) {
        return configurationDao.saveEntity(new ConfigurationEntity(conf)).getBean(Configuration.class);
    }

    @Override
    public List<Configuration> getList(String name) {
        List<ConfigurationEntity> entities = configurationDao.findEntityBy(ConfigurationEntity.class, "name", name);
        List<Configuration> beans = new ArrayList<Configuration>();
        for (ConfigurationEntity entity : entities) {
            beans.add(entity.getBean(Configuration.class));
        }
        return beans;
    }

    @Override
    public Configuration get(String name) {
        List<ConfigurationEntity> entities = configurationDao.findEntityBy(ConfigurationEntity.class, "name", name);
        if (entities.size() > 1)
            throw new RuntimeException("more than one value found for key " + name);
        if (entities.size() == 0)
            return null;
        return entities.get(0).getBean(Configuration.class);
    }

    @Override
    public Configuration get(String key, Object def) {
        Configuration conf = get(key);
        if (conf == null)
            conf = new Configuration(key, def.toString());
        return conf;
    }

    @Override
    public Configuration save(Object obj) {
        String json = JacksonUtil.obj2Json(obj);
        return save(obj.getClass().getName(), json);
    }

    @Override
    public Configuration save(String key, String value) {
        return save(new Configuration(key, value));
    }

    @Override
    public String getString(String key) {
        Configuration configuration = get(key);
        return configuration == null ? null : configuration.getValue();
    }

    @Override
    public String getString(String key, String defaultValue) {
        String value = getString(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public int getInt(String name, int port) {
        return Integer.parseInt(getString(name, port + ""));
    }

}
