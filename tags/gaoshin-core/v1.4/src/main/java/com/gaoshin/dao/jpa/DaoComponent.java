package com.gaoshin.dao.jpa;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * @author chzhang
 *
 */
public interface DaoComponent {
    void init();

    EntityManager getEntityManager();

    <T> T getEntity(Class<T> clazz, Object id);

    <T> List<T> findEntityBy(Class<T> clazz, String field, Object value);

    <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, int start, int size);

    <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1, int start,
            int size);

    <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1);

    <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1, String field2,
            Object value2, int start, int size);

    <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1, String field2,
            Object value2);

    <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, int start, int size, String orderBy);

    <T> List<T> findEntityBy(Class<T> clazz, HashMap<String, Object> params);

    List findEntityBy(String sql, Object... values);

    List findEntityBy(String sql, int start, int size, Object... values);

    <T> List<T> findEntityBy(Class<T> clazz, HashMap<String, Object> params, int start, int size);

    <T> T getFirstEntityBy(Class<T> clazz, String field, Object value);

    <T> T getFirstEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1);

    <T> T saveEntity(T entity);

    void removeEntity(Object entity);

    void removeEntity(Class clazz, Object pk);

    List<Object[]> nativeQuerySelect(String sql);

    List<Object[]> nativeQuerySelect(String sql, Object... parameters);

    int nativeQueryUpdate(String sql);

    <T> List<T> nativeQuerySelect(String sql, Class<T> clazz);

    <T> List<T> nativeQuerySelect(String sql, Class<T> clazz, int offset, int size);

    <T> List<T> nativeQuerySelect(String sql, int offset, int size);

    <T> List<T> nativeQuerySelect(String sql, Class<T> clazz, Object... parameters);

    Object nativeQuerySelectRow(String sql);

    <T> T nativeQuerySelectRow(String sql, Class<T> clazz);

    <T> List<T> nativeQuerySelect(String sql, Class<T> clazz, int start, int size, Object... parameters);
}
