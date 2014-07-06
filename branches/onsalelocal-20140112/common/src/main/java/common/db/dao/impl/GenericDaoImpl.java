package common.db.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import common.db.dao.GenericDao;
import common.db.util.ClassTree;
import common.db.util.ORMClass;
import common.util.reflection.ReflectionUtil;

public class GenericDaoImpl extends NamedParameterJdbcDaoSupport implements GenericDao {
    private static Map<Class, ORMClass> orm = new HashMap<Class, ORMClass>();
    
    public static synchronized ORMClass getOrmClass(Class cls) {
        ORMClass ormClass = orm.get(cls);
        if(ormClass == null) {
            ormClass = new ORMClass(cls);
            orm.put(cls, ormClass);
        }
        return ormClass;
    }
    
    public static ORMClass getOrmClass(Object obj) {
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
    public <T> List<T> queryBySql(Class<T> cls, String sql, Object... objs) {
        ORMClass oc = getOrmClass(cls);
        return oc.queryBySql(getJdbcTemplate(), sql, objs);
    }
    
    @Override
    public <T> T queryUniqueBySql(Class<T> cls, Map<String, ? extends Object> params, String sql) {
        ORMClass oc = getOrmClass(cls);
        return (T) oc.queryUniqueBySql(getNamedParameterJdbcTemplate(), sql, params);
    }
    
    @Override
    public <T> T queryUniqueBySql(Class<T> cls, Object[] params, String sql) {
        ORMClass oc = getOrmClass(cls);
        return (T) oc.queryUniqueBySql(getJdbcTemplate(), sql, params);
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
    
    @Override
    public Object getDetailsAndParents(ClassTree classTree, String id) {
        String sql = "select * from " + classTree.getThisClass().getSimpleName() + " where id=:id";
        List detailsAndParents = getDetailsAndParents(classTree, sql, Collections.singletonMap("id", id));
        if(detailsAndParents.size() == 0)
            return null;
        return detailsAndParents.get(0);
    }
    
    @Override
    public List getDetailsAndParents(ClassTree classTree, String sql, Map<String, ? extends Object> params) {
        List detailsList = new ArrayList();
        List list = queryBySql(classTree.getThisClass(), params, sql);
        Class detailClass = classTree.getDetailsClass();
        for(Object obj : list) {
            detailsList.add(ReflectionUtil.copy(detailClass, obj));
        }
        if(classTree.getParent() == null)
            return detailsList;
        
        List foreignValues = new ArrayList();
        for(Object obj : list) {
            try {
                Object value = ReflectionUtil.getFieldValue(obj, classTree.getParent().getIdFieldName());
                if(!foreignValues.contains(value)) {
                    foreignValues.add(value);
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        String parentSql = "select * from " + classTree.getParent().getThisClass().getSimpleName() + " where id in (:ids)";
        List parents = getDetailsAndParents(classTree.getParent(), parentSql, Collections.singletonMap("ids", foreignValues));
        Map<Object, Object> parentMap = new HashMap<Object, Object>();
        for(Object parent : parents) {
            try {
                parentMap.put(ReflectionUtil.getFieldValue(parent, "id"), parent);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        for(Object obj : detailsList) {
            try {
                Object key = ReflectionUtil.getFieldValue(obj, classTree.getParent().getIdFieldName());
                Object value = parentMap.get(key);
                if(value != null) {
                    ReflectionUtil.setField(obj, classTree.getParent().getDetailsFieldName(), value);
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        return detailsList;
    }
    
    @Override
    public Object getDetailsAndChildren(ClassTree classTree, String id) {
        String sql = "select * from " + classTree.getThisClass().getSimpleName() + " where id=:id";
        List detailsAndChildren = getDetailsAndChildren(classTree, sql, Collections.singletonMap("id", id));
        if(detailsAndChildren.size() == 0)
            return null;
        return detailsAndChildren.get(0);
    }
    
    @Override
    public List getDetailsAndChildren(ClassTree classTree, String sql, Map<String, ? extends Object> params) {
        List detailsList = new ArrayList();
        List list = queryBySql(classTree.getThisClass(), params, sql);
        Class detailClass = classTree.getDetailsClass();
        for(Object obj : list) {
            detailsList.add(ReflectionUtil.copy(detailClass, obj));
        }
        if(classTree.getChild() == null)
            return detailsList;
        
        Map<Object, Object> map = new HashMap<Object, Object>();
        List foreignValues = new ArrayList();
        for(Object obj : list) {
            try {
                Object value = ReflectionUtil.getFieldValue(obj, "id");
                foreignValues.add(value);
                map.put(value, obj);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        String idFieldName = classTree.getIdFieldName();
        String parentSql = "select * from " + classTree.getChild().getThisClass().getSimpleName() + " where " + idFieldName + " in (:ids)";
        List children = getDetailsAndChildren(classTree.getParent(), parentSql, Collections.singletonMap("ids", foreignValues));
        for(Object child : children) {
            try {
                Object parentId = ReflectionUtil.getFieldValue(child, idFieldName);
                Object parent = map.get(parentId);
                Object childDetailsList = ReflectionUtil.getFieldValue(parent, classTree.getChild().getDetailsListFieldName());
                List childList = (List) ReflectionUtil.getFieldValue(childDetailsList, "items");
                childList.add(child);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        return detailsList;
    }
    
    @Override
	public int update(String sql) {
		return getJdbcTemplate().update(sql);
	}
    
    @Override
	public int update(String sql, Object... args) {
		return getJdbcTemplate().update(sql, args);
	}
    
    @Override
	public int update(String sql, String[] names, Object[] values) {
		return getJdbcTemplate().update(sql, names, values);
	}

	@Override
    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
	    return getNamedParameterJdbcTemplate();
    }
}
