package common.xml.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import common.util.reflection.FieldFoundCallback;
import common.util.reflection.ReflectionUtil;

public class XmlUtil {
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static <T> T toJavaObject(InputStream inputStream, Class<T> clazz) {
        try {
            Document document = factory.newDocumentBuilder().parse(inputStream);
            Element documentElement = document.getDocumentElement();
            return toJavaObject(documentElement, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T toJavaObject(Element element, Class<T> clazz) throws Exception {
        if (element == null)
            return null;

        T object = clazz.newInstance();
        NodeList nlist = element.getChildNodes();
        if (nlist == null || nlist.getLength() == 0)
            return object;

        for (int i = 0; i < nlist.getLength(); i++) {

        }

        return null;
    }

    public static <T> T toJavaObject(String xmlString, Class<T> clazz) {
        try {
            if (xmlString == null || xmlString.length() == 0)
                return null;
            if (ReflectionUtil.isPrimeType(clazz)) {
                return (T) ReflectionUtil.convert(xmlString, clazz);
            }

            JSONObject xmlObject = new JSONObject(xmlString);
            if (clazz.equals(JSONObject.class))
                return (T) xmlObject;

            return toJavaObject(xmlObject, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toJavaObject(final JSONObject xmlObject, Class<T> clazz) throws Exception {
        final T javaObj = clazz.newInstance();
        ReflectionUtil.iterateFields(javaObj, new FieldFoundCallback() {
            public void field(Object javaObj, Field field) throws Exception {
                String fieldName = field.getName();
                if (!xmlObject.has(fieldName))
                    return;

                field.setAccessible(true);
                Object xmlFieldValue = xmlObject.get(fieldName);
                if (xmlFieldValue == null) {
                    return;
                }

                if (ReflectionUtil.isPrimeField(field)) {
                    ReflectionUtil.setFieldValue(javaObj, field, xmlFieldValue);
                }
                else if (field.getType().isAssignableFrom(List.class)) {
                    JSONArray xmlArray = null;
                    if (xmlFieldValue instanceof JSONArray) {
                        xmlArray = (JSONArray) xmlFieldValue;
                    }
                    else {
                        xmlArray.put(xmlFieldValue);
                    }
                    ArrayList<Object> list = new ArrayList<Object>();
                    field.setAccessible(true);
                    field.set(javaObj, list);
                    Class fieldGenericType = ReflectionUtil.getFieldGenericType(field);
                    for (int i = 0; i < xmlArray.length(); i++) {
                        Object xmlItem = xmlArray.get(i);
                        if (xmlItem == null)
                            continue;
                        if (ReflectionUtil.isPrimeType(fieldGenericType)) {
                            list.add(ReflectionUtil.convert(xmlItem.toString(), fieldGenericType));
                        }
                        else {
                            list.add(toJavaObject((JSONObject) xmlItem, fieldGenericType));
                        }
                    }
                }
                else if (field.getType().isAssignableFrom(Map.class)) {
                    Class fieldGenericType = ReflectionUtil.getFieldGenericType(field, 1);
                    JSONObject xmlMap = (JSONObject) xmlFieldValue;
                    JSONArray names = xmlMap.names();
                    Map<String, Object> javaMap = new HashMap<String, Object>();
                    field.setAccessible(true);
                    field.set(javaObj, javaMap);
                    for (int i = 0; i < names.length(); i++) {
                        String key = names.getString(i);
                        Object object = xmlMap.get(key);
                        if (object == null)
                            continue;
                        if (ReflectionUtil.isPrimeType(fieldGenericType)) {
                            javaMap.put(key, ReflectionUtil.convert(object.toString(), fieldGenericType));
                        }
                        else {
                            javaMap.put(key, toJavaObject((JSONObject) object, fieldGenericType));
                        }
                    }
                }
                else {
                    field.setAccessible(true);
                    field.set(javaObj, toJavaObject((JSONObject) xmlFieldValue, field.getType()));
                }
            }
        });
        return javaObj;
    }

    public static String toJsonString(Object javaObj) {
        JSONObject xmlObject = toJsonObject(javaObj);
        return xmlObject == null ? null : xmlObject.toString();
    }

    public static JSONObject toJsonObject(Object javaObj) {
        if (javaObj == null)
            return null;

        final JSONObject xml = new JSONObject();
        try {
            ReflectionUtil.iterateFields(javaObj, new FieldFoundCallback() {
                public void field(Object javaObj, Field field) throws Exception {
                    String fieldName = field.getName();
                    field.setAccessible(true);
                    Object fieldValue = field.get(javaObj);
                    if (fieldValue == null) {
                        return;
                    }

                    if (ReflectionUtil.isPrimeField(field)) {
                        xml.put(fieldName, ReflectionUtil.primeString(fieldValue));
                    }
                    else if (fieldValue instanceof List) {
                        JSONArray xmlArray = new JSONArray();
                        xml.put(fieldName, xmlArray);
                        List list = (List) fieldValue;
                        for (Object item : list) {
                            if (item == null)
                                continue;
                            if (ReflectionUtil.isPrimeType(item)) {
                                xmlArray.put(item);
                            }
                            else {
                                JSONObject xmlItem = toJsonObject(item);
                                xmlArray.put(xmlItem);
                            }
                        }
                    }
                    else if (fieldValue instanceof Map) {
                        JSONObject subxml = new JSONObject();
                        xml.put(fieldName, subxml);
                        Map<String, Object> map = (Map) fieldValue;
                        for (Entry<String, Object> entry : map.entrySet()) {
                            if (entry.getValue() == null)
                                continue;
                            if (ReflectionUtil.isPrimeType(entry.getValue())) {
                                subxml.put(entry.getKey(), entry.getValue());
                            }
                            else {
                                JSONObject xmlItem = toJsonObject(entry.getValue());
                                subxml.put(entry.getKey(), xmlItem);
                            }
                        }
                    }
                    else {
                        JSONObject subxml = toJsonObject(fieldValue);
                        xml.put(fieldName, subxml);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return xml;
    }

    public static Object get(Object xmlObj, String... keys) throws JSONException {
        return get((JSONObject) xmlObj, keys);
    }

    public static String getString(Object xmlObj, String... keys) throws JSONException {
        Object obj = get(xmlObj, keys);
        if (obj == null)
            return null;
        else
            return obj.toString();
    }

    public static Long getLong(Object xmlObj, String... keys) throws NumberFormatException, JSONException {
        return Long.parseLong(getString(xmlObj, keys));
    }

    public static Double getDouble(Object xmlObj, String... keys) throws NumberFormatException, JSONException {
        return Double.parseDouble(getString(xmlObj, keys));
    }

    public static Object get(JSONObject xmlObj, String... keys) throws JSONException {
        Object ret = xmlObj;
        for (String s : keys) {
            ret = ((JSONObject) ret).get(s);
        }
        return ret;
    }

    public static JSONArray getList(Object xmlObj, String... keys) throws JSONException {
        return (JSONArray) get(xmlObj, keys);
    }
}
