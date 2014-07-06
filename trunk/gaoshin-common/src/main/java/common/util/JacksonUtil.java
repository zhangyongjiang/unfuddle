package common.util;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

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
	
    public static <T> T jsonStr2Object(String json, Class<T> clazz) {
        try {
            StringReader sr = new StringReader(json);
            ObjectMapper processor = new ObjectMapper();
            T value = processor.readValue(sr, clazz);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonReader2Object(Reader reader, Class<T> clazz) {
        try {
            ObjectMapper processor = new ObjectMapper();
            T value = processor.readValue(reader, clazz);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonStream2Object(InputStream is, Class<T> clazz) {
        try {
            ObjectMapper processor = new ObjectMapper();
            T value = processor.readValue(is, clazz);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public static String obj2Json(Object obj, boolean indent) {
		ObjectMapper mapper = new ObjectMapper();
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
}
