package com.gaoshin.business;

import java.util.List;

import com.gaoshin.beans.Configuration;

public interface ConfigurationService {
    Configuration save(Configuration conf);

    Configuration save(String key, String value);

    Configuration save(Object obj);

    List<Configuration> getList(String key);

    Configuration get(String key);

    String getString(String key);

    String getString(String key, String defaultValue);

    Configuration get(String key, Object def);

    int getInt(String name, int port);
}
