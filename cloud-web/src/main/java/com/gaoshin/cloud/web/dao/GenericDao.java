package com.gaoshin.cloud.web.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao {
	void saveEntity(Object entity);

	void deleteEntity(Object entity);

	void nativeUpdate(String sql);

	<T> T getEntity(Class<T> entityClass, Serializable id);

	<T> List<T> list(Class<T> entityClass, String searchPath, Object value);

	<T> T getUniqueResult(Class<T> entityClass, String searchPath, Object value);

	<T> List<T> list(Class<T> class1, int offset, int size, String orderby, boolean asc);

	<T> List<T> list(Class<T> class1,
			String path, Object value, int offset, int size);

	<T> List<T> list(Class<T> class1,
			String path, Object value, int offset, int size, String orderby, boolean asc);

	<T> List<T> nativeQuery(Class<T> entityClass, String sql, Object[] values);

    <T> List<T> find(Class<T> entityClass, String query, Object value);

    List find(String query, Object... value);
    
    <T> List<T> find(Class<T> entityClass, int size, String hql, Object... value);
    
	<T> List<T> find(Class<T> entityClass, int offset, int size, String query,
			Object... value);

	<T> Long count(String field, boolean distinct, String query, Object... value);
	
	<T> Long max(String field, String query, Object... value);
	
	<T> Long min(String field, String query, Object... value);
	
	<T> Long avg(String field, String query, Object... value);
	
	<T> Long sum(String field, String query, Object... value);

    <T> List<T> find(Class<T> entityClass, String query);
    
    <T> List<T> in(Class<T> entityClass, String hql, List<Object> values);

    void merge(Object entity);
}
