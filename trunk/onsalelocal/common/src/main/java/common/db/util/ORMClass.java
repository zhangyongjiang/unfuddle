package common.db.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import common.util.reflection.FieldFoundCallback;
import common.util.reflection.ReflectionUtil;

public class ORMClass<T> {
    private Class<T> cls;
    private Map<String, Field> fields;
    private Map<String, Field> upper;
    private String columns;
    private String namedColumns;
    
    public class BaseRowMapper implements RowMapper<T> {

        private String[] queryColumns = null;

        @Override
        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            if(queryColumns == null) {
                ResultSetMetaData metaData = rs.getMetaData();
                queryColumns = new String[metaData.getColumnCount()];
                for(int i=0; i < metaData.getColumnCount(); i++) {
                    String label = metaData.getColumnLabel(i+1);
                    queryColumns[i] = label;
                }
            }
            
            try {
                T obj = cls.newInstance();
                for(int i=0; i<queryColumns.length; i++) {
                    String value = rs.getString(i+1);
                    Field field = getField(queryColumns[i]);
                    if(field == null)
                        continue;
                    Object fieldValue = ReflectionUtil.convert(value, field.getType());
                    field.set(obj, fieldValue);
                }
                return obj;
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public String getColumns() {
    	return columns;
    }
    
    private Field getField(String name) {
        Field field = fields.get(name);
        if(field != null)
            return field;
        field = upper.get(name.toUpperCase());
        if(field != null)
            return field;
        return null;
    }
    
    public ORMClass(Class<T> cls) {
        this.cls = cls;

        fields = new HashMap<String, Field>();
        upper = new HashMap<String, Field>();
        try {
            ReflectionUtil.iterateFields(cls, null, new FieldFoundCallback() {
                @Override
                public void field(Object o, Field field) throws Exception {
                    field.setAccessible(true);
                    fields.put(field.getName(), field);
                    upper.put(field.getName().toUpperCase(), field);
                }
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        StringBuilder sb = new StringBuilder();
        StringBuilder questionsMarks = new StringBuilder();
        StringBuilder namedColumns = new StringBuilder();
        questionsMarks.append("(");
        namedColumns.append("(");
        for(String key : fields.keySet()) {
            sb.append(key).append(",");
            questionsMarks.append("?,");
            namedColumns.append(":").append(key).append(",");
        }
        columns = sb.substring(0, sb.length()-1);
        this.namedColumns = namedColumns.substring(0, namedColumns.length() - 1) + ")";
    }
    
    public String getTableName() {
        return cls.getSimpleName();
    }
    
    public String getInsertHeader(boolean ignore) {
        if(ignore)
            return "insert ignore into " + getTableName() + " ";
        else
            return "insert into " + getTableName() + " ";
    }
    
    public String getNamedInsertStmt(boolean ignore) {
        return getInsertHeader(ignore) + " (" + columns + ") values " + this.namedColumns; 
    }

    public String getNamedReplaceStmt() {
        return "replace into " + getTableName() + " (" + columns + ") values " + this.namedColumns; 
    }

    public Map<String, Object> getValueMap(Object obj) {
        Map<String, Object> values = new HashMap<String, Object>();
        for(Entry<String, Field> entry : fields.entrySet()) {
            String column = entry.getKey();
            Field field = entry.getValue();
            Object value = null;
            try {
                value = field.get(obj);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(field.getType().isEnum()) {
                values.put(column, value == null ? value : value.toString());
            }
            else {
                values.put(column, value);
            }
        }
        
        return values;
    }
    
    public void insert(NamedParameterJdbcTemplate jc, Object obj, boolean ignore) {
        String sql = getNamedInsertStmt(ignore);
        insertOrReplace(jc, sql, obj);
    }
    
    public void insertOrReplace(NamedParameterJdbcTemplate jc, String sql, Object obj) {
        Map<String, Object> values = getValueMap(obj);
        long now = System.currentTimeMillis();
        if(values.containsKey("created")) {
            values.put("created", now);
            try {
                fields.get("created").set(obj, now);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(values.containsKey("updated")) {
            values.put("updated", now);
            try {
                fields.get("updated").set(obj, now);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Object id = values.get("id");
        if(id == null) {
            id = GUID.getRandomStringUuid((short) 0);
            values.put("id", id);
            try {
                ReflectionUtil.setField(obj, "id", id);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        jc.update(sql, values);
    }
    
    public T getById(NamedParameterJdbcTemplate jc, String id) {
        return getById(jc, id, "*");
    }
    
    public T getById(NamedParameterJdbcTemplate jc, String id, String... columns) {
    	if(false) 
    		return getUniqueResult(jc, Collections.singletonMap("id", id), columns);
        
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(SqlUtil.getStrColumns(columns));
        sql.append(" from ").append(getTableName());
        sql.append(" where id=:id limit :limit");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("limit", 2);
        List list = queryBySql(jc, sql.toString(), params);
        if(list.size() == 0) return null;
        if(list.size()>1) 
        	System.out.println("!!!!!!!!! more than one record found for " + getTableName() + " " + id);
        return (T) list.get(0);
    }

    public T getUniqueResult(NamedParameterJdbcTemplate jc, Map<String, String> where, String... columns) {
        List<T> list = query(jc, where, columns);
        if(list.size() == 0) return null;
        if(list.size() > 1) throw new RuntimeException("found more than one record for " + where.toString());
        return list.get(0);
    }

    public List<T> query(NamedParameterJdbcTemplate jc, Map<String, String> where, String... columns) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(SqlUtil.getStrColumns(columns));
        sql.append(" from ").append(getTableName());
        
        if(where != null && where.size() > 0) {
            sql.append(" where ");
            boolean first = true;
            for(String key : where.keySet()) {
                if(first) {
                    first = false;
                }
                else {
                    sql.append(" AND ");
                }
                sql.append(key).append("=:").append(key);
            }
        }
        
        List<T> result = jc.query(sql.toString(), where, new BaseRowMapper());
        return result;
    }

    public void update(NamedParameterJdbcTemplate jc, Object obj, String[] updateColumns, String[] whereColumns) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("update ").append(getTableName()).append(" set ");
            boolean first = true;
            if(fields.containsKey("updated")) {
                first = false;
                long time = System.currentTimeMillis();
                sql.append(" updated = ").append(time);
                fields.get("updated").set(obj, time);
            }
            Map values = new HashMap();
            for(String column : updateColumns) {
                if(first) {
                    first = false;
                }
                else {
                    sql.append(",");
                }
                sql.append(column).append("=:").append(column).append(" ");
                Field field = fields.get(column);
                Object value = field.get(obj);
                if(field.getType().isEnum()) {
                    values.put(column, value == null ? value : value.toString());
                }
                else {
                    values.put(column, value);
                }
            }
            if(whereColumns != null && whereColumns.length>0) {
                sql.append(" where ");
                first = true;
                for(String column : whereColumns) {
                    if(first) {
                        first = false;
                    }
                    else {
                        sql.append(" and ");
                    }
                    sql.append(column).append("=:").append(column).append(" ");
                    Field field = fields.get(column);
                    Object value = field.get(obj);
                    if(field.getType().isEnum()) {
                        values.put(column, value == null ? value : value.toString());
                    }
                    else {
                        values.put(column, value);
                    }
                }
            }
            jc.update(sql.toString(), values);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEntity(NamedParameterJdbcTemplate jc, Object obj, String[] updateColumns) {
        if(updateColumns == null || updateColumns.length == 0 || updateColumns.equals("*"))
            updateColumns = columns.split(",");
        update(jc, obj, updateColumns, new String[]{"id"});
    }

    public void delete(NamedParameterJdbcTemplate jc, Map<String, ?> values) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from ").append(getTableName()).append(" ");
            boolean first = true;
            if(values != null && values.size() > 0) {
                sql.append(" where ");
                first = true;
                for(String column : values.keySet().toArray(new String[0])) {
                    if(first) {
                        first = false;
                    }
                    else {
                        sql.append(" and ");
                    }
                    sql.append(column).append("=:").append(column).append(" ");
                }
            }
            jc.update(sql.toString(), values);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(NamedParameterJdbcTemplate jc, Map<String, ? extends Object> values, Map<String, ? extends Object> where) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuilder sql = new StringBuilder();
            sql.append("update ").append(getTableName()).append(" set ");
            boolean first = true;
            if(fields.containsKey("updated")) {
                first = false;
                long time = System.currentTimeMillis();
                sql.append(" updated = ").append(time);
            }
            for(Entry<String, ? extends Object> entry : values.entrySet()) {
                String column = entry.getKey();
                Object value = entry.getValue();
                if(first) {
                    first = false;
                }
                else {
                    sql.append(",");
                }
                String paramName = "u" + column;
                sql.append(column).append("=:").append(paramName).append(" ");
                Field field = fields.get(column);
                if(field.getType().isEnum()) {
                    params.put(paramName, value == null ? value : value.toString());
                }
                else {
                    params.put(paramName, value);
                }
            }
            if(where != null && where.size() > 0) {
                sql.append(" where ");
                first = true;
                for(Entry<String, ? extends Object> entry : where.entrySet()) {
                    String column = entry.getKey();
                    Object value = entry.getValue();
                    if(first) {
                        first = false;
                    }
                    else {
                        sql.append(" and ");
                    }
                    sql.append(column).append("=:").append(column).append(" ");
                    Field field = fields.get(column);
                    if(field.getType().isEnum()) {
                        params.put(column, value == null ? value : value.toString());
                    }
                    else {
                        params.put(column, value);
                    }
                }
            }
            jc.update(sql.toString(), params);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <C> List<C> queryColumn(NamedParameterJdbcTemplate jc, String column, Map where) {
        List<?> objects = query(jc, where, column);
        List<C> result = new ArrayList<C>();
        Field field = fields.get(column);
        for(Object obj : objects) {
            try {
                Object value = field.get(obj);
                result.add((C) value);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public T queryUniqueBySql(NamedParameterJdbcTemplate npjc, String sql, Map params) {
        List<T> result = queryBySql(npjc, sql, params);
        if(result.size() == 0)
            return null;
        if(result.size() > 1)
            throw new RuntimeException("more than one record found for " + sql + ", " + params);
        return result.get(0);
    }

    public T queryUniqueBySql(JdbcTemplate npjc, String sql, Object[] params) {
        List<T> result = queryBySql(npjc, sql, params);
        if(result.size() == 0)
            return null;
        if(result.size() > 1)
            throw new RuntimeException("more than one record found for " + sql + ", " + params);
        return result.get(0);
    }

    public List<T> queryBySql(NamedParameterJdbcTemplate npjc, String sql, Map params) {
        List<T> result = npjc.query(sql, params, new BaseRowMapper());
        return result;
    }

    public List<T> queryBySql(JdbcTemplate npjc, String sql, Object[] params) {
        List<T> result = npjc.query(sql, params, new BaseRowMapper());
        return result;
    }

    public void replace(NamedParameterJdbcTemplate jc, Object obj) {
        String sql = getNamedReplaceStmt();
        insertOrReplace(jc, sql, obj);
    }
}
