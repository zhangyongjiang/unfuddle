package common.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.type.TypeReference;

public class JacksonUtil {
    public static TypeReference<HashMap<String, Object>> getTypeRef() {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        return typeRef;
    }
    
	public static String obj2Json(Object obj) {
		return obj2Json(obj, false);
	}
	
	public static String obj2Json(Object obj, boolean indent) {
	    if(obj == null)
	        return null;
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.WRITE_NULL_PROPERTIES, false);
		if(indent)
			mapper.configure(Feature.INDENT_OUTPUT, true); 
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sw.toString();
	}
	
	public static String toString(Object jsonObj, ObjectMapper mapper) {
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, jsonObj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sw.toString();
	}
	
	public static Object get(Object jsonObj, String... keys) {
		return get((HashMap<String, Object>)jsonObj, keys);
	}
	
	public static String getString(Object jsonObj, String... keys) {
		Object obj = get(jsonObj, keys);
		if(obj == null)
			return null;
		else
			return obj.toString();
	}
	
	public static Long getLong(Object jsonObj, String... keys) {
		return Long.parseLong(getString(jsonObj, keys));
	}
	
	public static Double getDouble(Object jsonObj, String... keys) {
		return Double.parseDouble(getString(jsonObj, keys));
	}
	
	public static Object get(HashMap<String, Object> jsonObj, String... keys) {
		Object ret = jsonObj;
		for(String s : keys) {
			ret = ((HashMap<String, Object>)ret).get(s);
		}
		return ret;
	}
	
	public static List<Object> getList(Object jsonObj, String... keys) {
		return (List<Object>) get(jsonObj, keys);
	}

    public static <T> T json2Object(String str, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        StringReader sr = new StringReader(str);
        ObjectMapper jsonProcessor = new ObjectMapper();
        jsonProcessor.configure(Feature.WRITE_NULL_PROPERTIES, false);
        jsonProcessor.configure(
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return (T)jsonProcessor.readValue(sr, clazz);
    }

    public static <T> T json2Object(String str, TypeReference typeref) throws JsonParseException, JsonMappingException, IOException {
        StringReader sr = new StringReader(str);
        ObjectMapper jsonProcessor = new ObjectMapper();
        jsonProcessor.configure(Feature.WRITE_NULL_PROPERTIES, false);
        jsonProcessor.configure(
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return (T)jsonProcessor.readValue(sr, typeref);
    }

    public static <T> List<T> json2List(String str, TypeReference<List<T>> typeRef) throws JsonParseException, JsonMappingException, IOException {
        StringReader sr = new StringReader(str);
        ObjectMapper jsonProcessor = new ObjectMapper();
        jsonProcessor.configure(Feature.WRITE_NULL_PROPERTIES, false);
        jsonProcessor.configure(
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return jsonProcessor.readValue(sr, typeRef);
    }
}
