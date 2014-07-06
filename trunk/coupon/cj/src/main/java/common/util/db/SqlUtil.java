package common.util.db;

import java.lang.reflect.Field;
import java.util.List;

import common.util.CamelUnderScore;
import common.util.reflection.FieldFoundCallback;
import common.util.reflection.ReflectionUtil;

public class SqlUtil {
    public static String getInsertStatement(List<?> list, boolean insertIgnore) throws Exception {
        String ret = null;
        if(list.size()>0){
            final StringBuilder sb = new StringBuilder();
            sb.append(getInsertHeader(list.get(0), insertIgnore));
            for(Object obj : list){
                sb.append(getInsertValues(obj)+",");
            }
            ret = sb.substring(0,sb.length()-1);
        }
        return ret;
    }
    
    public static String getInsertStatement(Object obj) throws Exception {
        return getInsertHeader(obj)+getInsertValues(obj);
    }
    
    public static String getInsertIgnoreStatement(Object obj) throws Exception {
        return getInsertHeader(obj, true)+getInsertValues(obj);
    }
    
    public static String getInsertHeader(Object obj, boolean insertIgnore) throws Exception {
        final StringBuilder sb = new StringBuilder();
        Class<?> beanClass = obj.getClass();
        ReflectionUtil.iterateFields(beanClass, obj, new FieldFoundCallback() {
            @Override
            public void field(Object o, Field field) throws Exception {
                if(sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(CamelUnderScore.underscore(field.getName()));
            }
        });
        String table = CamelUnderScore.underscore(obj.getClass().getSimpleName());
        if(insertIgnore)
            return "insert ignore into "+table+" ("+sb.toString()+") VALUES ";
        else
            return "insert into "+table+" ("+sb.toString()+") VALUES ";
    }
    
    public static String getInsertValues(Object obj) throws Exception {
        final StringBuilder sb = new StringBuilder();
        Class<?> beanClass = obj.getClass();
        ReflectionUtil.iterateFields(beanClass, obj, new FieldFoundCallback() {
            @Override
            public void field(Object o, Field field) throws Exception {
                field.setAccessible(true);
                Object fieldValue = field.get(o);
                if(sb.length() > 0) {
                    sb.append(", ");
                }
                Class<?> fieldType = field.getType();
                if(String.class.equals(fieldType) && fieldValue != null) {
                    String value = ((String)fieldValue).replaceAll("'", "''");
                    value = value.replaceAll("\"", "\\\"");
                    sb.append("\""+value+"\"");
                }
                else if(fieldType.isEnum() && fieldValue != null) {
                    sb.append("'"+fieldValue+"'");
                }
                else {
                    sb.append(fieldValue);
                }
            }
        });
        return "("+sb.toString()+")";
    }
    
    public static String getUpdateStatement(Object obj, String idFieldName) throws Exception {
        final StringBuilder sb = new StringBuilder();
        Class<?> beanClass = obj.getClass();
        ReflectionUtil.iterateFields(beanClass, obj, new FieldFoundCallback() {
            @Override
            public void field(Object o, Field field) throws Exception {
                field.setAccessible(true);
                Object fieldValue = field.get(o);
                if(fieldValue == null)
                    return;
                if(sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(CamelUnderScore.underscore(field.getName()));
                sb.append("=");
                Class<?> fieldType = field.getType();
                if(String.class.equals(fieldType)) {
                    String value = ((String)fieldValue).replaceAll("'", "''");
                    value = value.replaceAll("\"", "\\\"");
                    sb.append("\""+value+"\"");
                }
                else if(fieldType.isEnum()) {
                    sb.append("'"+fieldValue+"'");
                }
                else {
                    sb.append(fieldValue);
                }
            }
        });
        String table = CamelUnderScore.underscore(obj.getClass().getSimpleName());
        String id = (String) ReflectionUtil.getFieldValue(obj, idFieldName);
        sb.append(" where ").append(CamelUnderScore.underscore(idFieldName)).append("='").append(id).append("'");
        return "update " + table + " set " + sb.toString();
    }
    
    public static String getDeleteStatement(Object obj, String idFieldName) throws Exception {
        final StringBuilder sb = new StringBuilder();
        String table = CamelUnderScore.underscore(obj.getClass().getSimpleName());
        String id = (String) ReflectionUtil.getFieldValue(obj, idFieldName);
        sb.append("delete from "+table);
        sb.append(" where ").append(CamelUnderScore.underscore(idFieldName)).append("='").append(id).append("'");
        return sb.toString();
    }
    
    public static String getDeleteStatementIn(Class<?> clazz, String idFieldName, String in) throws Exception {
        String table = CamelUnderScore.underscore(clazz.getSimpleName());
        String field = CamelUnderScore.underscore(idFieldName);
        return "delete from "+table+" where "+field+" in ("+in+")";
    }

    public static String getInsertStatement(List<?> list) throws Exception {
        return getInsertStatement(list, false);
    }
    
    public static String getInsertIgnoreStatement(List<?> list) throws Exception {
        return getInsertStatement(list, true);
    }
    
    public static String getInsertHeader(Object obj) throws Exception {
        return getInsertHeader(obj, false);
    }

    public static String join(String[] ids) {
        return join(",",ids);
    }
    
    public static String join(String delimeter, String[] ids){
        StringBuffer sb = new StringBuffer(ids.length * 8);
        for (String s : ids) {
            sb.append(delimeter+"'" + s + "'");
        }
        return sb.substring(delimeter.length());
    }
    
    public static String getStrColumns(String... columns) {
        if(columns == null || columns.length == 0)
            return "*";
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<columns.length; i++) {
            if(i > 0)
                sb.append(",");
            sb.append(columns[i]);
        }
        return sb.toString();
    }
}
