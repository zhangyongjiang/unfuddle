package com.gaoshin.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;

public class DaoComponentImpl implements DaoComponent {
    @Value("${jdbc.driver.className}")
    private String database;

    protected EntityManager entityManager = null;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void init() {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public <T> T getEntity(Class<T> clazz, Object id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public <T> T saveEntity(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, String field, Object value) {
        return findEntityBy(clazz, field, value, -1, -1);
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, HashMap<String, Object> params) {
        return findEntityBy(clazz, params, -1, -1);
    }

    public <T> List<T> findEntityBy(Class<T> clazz, HashMap<String, Object> params, int start, int size, String orderBy) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select o from ").append(clazz.getSimpleName()).append(" o where ");

        boolean first = true;
        for (Entry<String, Object> entry : params.entrySet()) {
            if (!first) {
                jpql.append(" and ");
            }
            jpql.append("o.").append(entry.getKey()).append("=:").append(entry.getKey().replace('.', '_'));
            first = false;
        }

        if (orderBy != null) {
            jpql.append(" order by o.").append(orderBy);
        }

        Query query = getEntityManager().createQuery(jpql.toString());
        for (Entry<String, Object> entry : params.entrySet()) {
            String field = entry.getKey().replace('.', '_');
            Object value = entry.getValue();
            query.setParameter(field, value);
        }

        if (start >= 0) {
            query.setFirstResult(start);
        }

        if (size > 0) {
            query.setMaxResults(size);
        }

        return query.getResultList();
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, HashMap<String, Object> params, int start, int size) {
        return findEntityBy(clazz, params, start, size, null);
    }

    @Override
    public <T> T getFirstEntityBy(Class<T> clazz, String field, Object value) {
        List<T> list = findEntityBy(clazz, field, value);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <T> T getFirstEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(field, value);
        params.put(field1, value1);
        List<T> list = findEntityBy(clazz, params, 0, 1);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, int start, int size) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(field, value);
        return findEntityBy(clazz, params, start, size);
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1,
            int start, int size) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(field, value);
        params.put(field1, value1);
        return findEntityBy(clazz, params, start, size);
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1,
            String field2, Object value2, int start, int size) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(field, value);
        params.put(field1, value1);
        params.put(field2, value2);
        return findEntityBy(clazz, params, start, size);
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1,
            String field2, Object value2) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(field, value);
        params.put(field1, value1);
        params.put(field2, value2);
        return findEntityBy(clazz, params);
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, String field1, Object value1) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(field, value);
        params.put(field1, value1);
        return findEntityBy(clazz, params);
    }

    @Override
    public <T> List<T> findEntityBy(Class<T> clazz, String field, Object value, int start, int size, String orderBy) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(field, value);
        return findEntityBy(clazz, params, start, size, orderBy);
    }

    @Override
    public void removeEntity(Object entity) {
        entityManager.remove(entity);
    }

    @Override
    public void removeEntity(Class clazz, Object pk) {
        Object entity = entityManager.find(clazz, pk);
        removeEntity(entity);
    }

    protected String getInSql(List list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(list.get(i).toString());
        }
        return sb.toString();
    }

    @Override
    public List<Object[]> nativeQuerySelect(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        return nativeQuery.getResultList();
    }

    @Override
    public List<Object[]> nativeQuerySelect(String sql, int offset, int size) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setFirstResult(offset);
        nativeQuery.setMaxResults(size);
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> nativeQuerySelect(String sql, Class<T> clazz) {
        Query nativeQuery = entityManager.createNativeQuery(sql, clazz);
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> nativeQuerySelect(String sql, Class<T> clazz, int offset, int size) {
        Query nativeQuery = entityManager.createNativeQuery(sql, clazz);
        nativeQuery.setFirstResult(offset);
        nativeQuery.setMaxResults(size);
        return nativeQuery.getResultList();
    }

    @Override
    public Object nativeQuerySelectRow(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        return nativeQuery.getSingleResult();
    }

    @Override
    public <T> T nativeQuerySelectRow(String sql, Class<T> clazz) {
        Query nativeQuery = entityManager.createNativeQuery(sql, clazz);
        return (T) nativeQuery.getSingleResult();
    }

    @Override
    public int nativeQueryUpdate(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        return nativeQuery.executeUpdate();
    }

    protected boolean isMysql() {
        return database.toLowerCase().indexOf("mysql") != -1;
    }

    protected boolean isH2() {
        return database.toLowerCase().indexOf("org.h2") != -1;
    }

    @Override
    public <T> List<T> nativeQuerySelect(String sql, Class<T> clazz, Object... parameters) {
        Query nativeQuery = entityManager.createNativeQuery(sql, clazz);
        for (int i = 0; i < parameters.length; i++) {
            nativeQuery.setParameter(i + 1, parameters[i]);
        }
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> nativeQuerySelect(String sql, Class<T> clazz, int start, int size, Object... parameters) {
        Query nativeQuery = entityManager.createNativeQuery(sql, clazz);
        for (int i = 0; i < parameters.length; i++) {
            nativeQuery.setParameter(i + 1, parameters[i]);
        }
        nativeQuery.setFirstResult(start);
        nativeQuery.setMaxResults(size);
        return nativeQuery.getResultList();
    }

    @Override
    public List<Object[]> nativeQuerySelect(String sql, Object... parameters) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        for (int i = 0; i < parameters.length; i++) {
            nativeQuery.setParameter(i + 1, parameters[i]);
        }
        return nativeQuery.getResultList();
    }

    @Override
    public List findEntityBy(String sql, Object... values) {
        Query query = entityManager.createQuery(sql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query.getResultList();
    }

    @Override
    public List findEntityBy(String sql, int start, int size, Object... values) {
        Query query = entityManager.createQuery(sql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i + 1, values[i]);
        }
        query.setFirstResult(start);
        query.setMaxResults(size);
        return query.getResultList();
    }
}
