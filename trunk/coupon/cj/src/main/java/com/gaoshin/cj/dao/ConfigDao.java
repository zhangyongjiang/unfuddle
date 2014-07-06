package com.gaoshin.cj.dao;


public interface ConfigDao extends GenericDao {
    String get(String name);
    String get(String name, Object defaultValue);
    int getInt(String name, int defaultValue);
    long getLong(String name, long defaultValue);
    float getFloat(String name, float defaultValue);
    void set(String name, Object value);
}
