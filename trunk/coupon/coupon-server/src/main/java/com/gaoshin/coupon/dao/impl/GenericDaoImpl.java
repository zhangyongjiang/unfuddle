package com.gaoshin.coupon.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.gaoshin.coupon.dao.GenericDao;
import common.util.db.ORMClass;

public class GenericDaoImpl extends NamedParameterJdbcDaoSupport implements GenericDao {
    private static Map<Class, ORMClass> orm = new HashMap<Class, ORMClass>();
    
    private static synchronized ORMClass getOrmClass(Class cls) {
        ORMClass ormClass = orm.get(cls);
        if(ormClass == null) {
            ormClass = new ORMClass(cls);
            orm.put(cls, ormClass);
        }
        return ormClass;
    }
    private static ORMClass getOrmClass(Object obj) {
        return getOrmClass(obj.getClass());
    }
    
    @Autowired
    public void init(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void insert(Object obj) {
        insert(obj, false);
    }

    @Override
    public void insert(Object obj, boolean ignore) {
        ORMClass oc = getOrmClass(obj);
        oc.insert(getNamedParameterJdbcTemplate(), obj, ignore);
    }
    
    @Override
    public <T> T getEntity(Class<T> cls, String id, String... columns) {
        if(columns == null || columns.length == 0)
            columns = new String[]{"*"};
        ORMClass oc = getOrmClass(cls);
        return (T) oc.getById(getNamedParameterJdbcTemplate(), id, columns);
    }
    
    @Override
    public <T> T getUniqueResult(Class<T> cls, Map<String, ? extends Object> where) {
        return getUniqueResult(cls, where, "*");
    }
    
    @Override
    public <T> T getUniqueResult(Class<T> cls, Map<String, ? extends Object> where, String... columns) {
        List<T> list = query(cls, where, columns);
        if(list.size() == 0)
            return null;
        if(list.size() > 1)
            throw new RuntimeException("duplicated records found for " + where);
        return list.get(0);
    }
    
    @Override
    public <T> List<T> query(Class<T> cls, Map<String, ? extends Object> where) {
        return query(cls, where, "*");
    }
    
    @Override
    public <T> List<T> query(Class<T> cls, Map<String, ? extends Object> where, String... columns) {
        ORMClass oc = getOrmClass(cls);
        return oc.query(getNamedParameterJdbcTemplate(), where, columns);
    }
    
    @Override
    public <T> List<T> queryBySql(Class<T> cls, Map<String, ? extends Object> params, String sql) {
        ORMClass oc = getOrmClass(cls);
        return oc.queryBySql(getNamedParameterJdbcTemplate(), sql, params);
    }
    
    @Override
    public <T> T queryUniqueBySql(Class<T> cls, Map<String, ? extends Object> params, String sql) {
        ORMClass oc = getOrmClass(cls);
        return (T) oc.queryUniqueBySql(getNamedParameterJdbcTemplate(), sql, params);
    }
    
    @Override
    public <T> List<T> queryColumn(Class<?> cls, Map<String, ? extends Object> where, String column) {
        ORMClass oc = getOrmClass(cls);
        return oc.queryColumn(getNamedParameterJdbcTemplate(), column, where);
    }
    
    @Override
    public void updateEntity(Object obj, String... columns) {
        ORMClass oc = getOrmClass(obj);
        oc.updateEntity(getNamedParameterJdbcTemplate(), obj, columns);
    }
    
    @Override
    public void delete(Class class1, Map values) {
        ORMClass oc = getOrmClass(class1);
        oc.delete(getNamedParameterJdbcTemplate(), values);
    }
    
    @Override
    public void update(Class<?> class1, Map<String, ? extends Object> values, Map<String, ? extends Object> where) {
        ORMClass oc = getOrmClass(class1);
        oc.update(getNamedParameterJdbcTemplate(), values, where);
    }
    
    @Override
    public void replace(Object obj) {
        ORMClass oc = getOrmClass(obj);
        oc.replace(getNamedParameterJdbcTemplate(), obj);
    }
}
