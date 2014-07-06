package com.gaoshin.appbooster.dao;

import java.util.List;
import java.util.Map;

import common.util.db.ClassTree;

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
    Object getDetailsAndParents(ClassTree classTree, String id);
    List getDetailsAndParents(ClassTree classTree, String sql, Map<String, ? extends Object> params);
    Object getDetailsAndChildren(ClassTree classTree, String id);
    List getDetailsAndChildren(ClassTree classTree, String sql, Map<String, ? extends Object> params);
}
