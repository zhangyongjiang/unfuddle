package com.gaoshin.coupon.dao;

import java.util.List;
import java.util.Map;

public interface GenericDao {
    void insert(Object obj);
    void replace(Object obj);
    void insert(Object obj, boolean ignore);
    <T> T getEntity(Class<T> cls, String id, String... columns);
    <T> T getUniqueResult(Class<T> cls, Map<String, ? extends Object> where, String... columns);
    <T> List<T> query(Class<T> cls, Map<String, ? extends Object> where, String... columns);
    <T> T getUniqueResult(Class<T> cls, Map<String, ? extends Object> where);
    <T> List<T> query(Class<T> cls, Map<String, ? extends Object> where);
    void updateEntity(Object obj, String... columns);
    void delete(Class class1, Map values);
    void update(Class<?> class1, Map<String, ? extends Object> values, Map<String, ? extends Object> where);
    <T> List<T> queryColumn(Class<?> cls, Map<String, ? extends Object> where, String column);
    <T> List<T> queryBySql(Class<T> cls, Map<String, ? extends Object> params, String sql);
    <T> T queryUniqueBySql(Class<T> cls, Map<String, ? extends Object> params, String sql);
}
