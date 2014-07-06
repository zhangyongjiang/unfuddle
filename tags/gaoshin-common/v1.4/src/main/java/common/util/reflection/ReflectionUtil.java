package common.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import common.util.DateUtil;

public class ReflectionUtil {
    public static <T> T copy(Class<T> clazz, Object from) {
        Object to = null;;
        try {
            to = clazz.newInstance();
            copy(to, from, false);
        } catch (Exception e) {
        }
        return (T) to;
    }

    public static void copy(Object to, Object from) {
        copy(to, from, false);
    }

    public static void copy(Object to, Object from, boolean ignoreNull) {
        if ((from == null) || (to == null)) {
            return;
        }
        Class<? extends Object> toClass = to.getClass();
        Class<? extends Object> fromClass = from.getClass();
        Field[] fields = null;
        if (fromClass.toString().indexOf("$") == -1) {
            fields = fromClass.getDeclaredFields();
        } else {
            fields = toClass.getDeclaredFields();
        }
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> fromType = field.getType();
            Object fromValue = null;
            String name = field.getName();
            if (name.indexOf('$') != -1) {
                // ignore proxy
                continue;
            }
            try {
                String methodPrefix = "get";
                if(fromType.equals(Boolean.class) || fromType.equals(boolean.class)) {
                    methodPrefix = "is";
                }
                String getterName = methodPrefix + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = fromClass.getMethod(getterName, null);
                fromValue = method.invoke(from, null);
                // fromValue = field.get(from);
                if (ignoreNull) {
                    if (fromValue == null) {
                        continue;
                    }
                    if (fromValue instanceof String) {
                        if (fromValue.toString().length() == 0) {
                            continue;
                        }
                    }
                }
            } catch (Exception e1) {
            }
            try {
                String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = toClass.getMethod(setterName, fromType);
                method.invoke(to, fromValue);
                // Field toField = toClass.getDeclaredField(name);
                // Class<?> toType = toField.getType();
                // if (!fromType.equals(toType)) {
                // continue;
                // }
                // toField.setAccessible(true);
                // toField.set(to, fromValue);
            } catch (Exception e) {
            }
        }
    }

    public static <T> T copyPrimeProperties(Class<T> toClazz, Object from) {
        if (from == null)
            return null;
        try {
            T to = toClazz.newInstance();
            copyPrimeProperties(to, from);
            return to;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyPrimeProperties(final Object to, final Object from) {
        iterateFields(to, new FieldFoundCallback() {
            @Override
            public void field(Object o, Field field) {
                if (isPrimeType(field.getType())) {
                    String fieldName = field.getName();
                    Field fromField = getField(from.getClass(), fieldName);
                    if (fromField == null)
                        return;
                    field.setAccessible(true);
                    fromField.setAccessible(true);
                    if (fromField.getType().equals(field.getType())) {
                        try {
                            field.set(to, fromField.get(from));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    public static <T> T copy(Class<T> toClass, final Object from, final Class... views) {
        if (from == null)
            return null;
        T to;
        try {
            to = toClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        copy(to, from, views);
        return to;
    }

    public static void copy(final Object to, final Object from, final Class... views) {
        iterateFields(to, new FieldFoundCallback() {
            @Override
            public void field(Object o, Field field) {
                String fieldName = field.getName();
                Field fromField = getField(from.getClass(), fieldName);
                if (fromField == null)
                    return;
                field.setAccessible(true);
                fromField.setAccessible(true);
                if (isPrimeType(field.getType())) {
                    if (fromField.getType().equals(field.getType())) {
                        try {
                            field.set(to, fromField.get(from));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                else {
                    Views anno = (Views) getAnnotation(field, Views.class);
                    if (anno == null)
                        return;

                    for (Class exclude : anno.excludes()) {
                        for (Class v : views) {
                            if (v.equals(exclude)) {
                                return;
                            }
                        }
                    }

                    boolean copy = false;
                    for (Class include : anno.value()) {
                        for (Class v : views) {
                            if (v.equals(include) || Views.ANY.equals(include)) {
                                copy = true;
                                break;
                            }
                        }
                        if (copy)
                            break;
                    }

                    if (copy) {
                        if (List.class.isAssignableFrom(field.getType())) {
                            if (List.class.isAssignableFrom(fromField.getType())) {
                                try {
                                    Object fromFieldValue = fromField.get(from);
                                    if (fromFieldValue == null) {
                                        field.set(to, null);
                                    }
                                    else {
                                        List toFieldValue = new ArrayList();
                                        field.set(to, toFieldValue);
                                        List fromList = (List) fromFieldValue;
                                        for (Object obj : fromList) {
                                            if (obj == null)
                                                toFieldValue.add(null);
                                            else {
                                                Object toFieldItem = getFieldGenericType(field).newInstance();
                                                toFieldValue.add(toFieldItem);
                                                copy(toFieldItem, obj, views);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else
                                throw new RuntimeException(fieldName + " is not a List type");
                        }
                        else {
                            try {
                                Object fromFieldValue = fromField.get(from);
                                if (fromFieldValue == null) {
                                    field.set(to, null);
                                }
                                else {
                                    Object toFieldValue = field.getType().newInstance();
                                    copy(toFieldValue, fromFieldValue, views);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void iterateClassTree(Class<?> clazz, ClassCallback callback) {
        while (true) {
            if (clazz.equals(Object.class)) {
                break;
            }
            callback.classFound(clazz);
            clazz = clazz.getSuperclass();
        }
    }

    public static void iterateFields(final Object o, final FieldFoundCallback callback) {
        iterateFields(o.getClass(), o, callback);
    }

    private static void iterateFields(final Class<?> clazz, final Object o, final FieldFoundCallback callback)
 {
        iterateClassTree(clazz, new ClassCallback() {
            public void classFound(Class<?> cls) {
                for (Field field : cls.getDeclaredFields()) {
                    try {
                        callback.field(o, field);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public static void iterateMethods(final Object o, final MethodFoundCallback callback) {
        iterateMethods(o.getClass(), o, callback);
    }

    private static void iterateMethods(final Class<?> clazz, final Object o, final MethodFoundCallback callback)
 {
        iterateClassTree(clazz, new ClassCallback() {
            public void classFound(Class<?> cls) {
                for (Method method : cls.getDeclaredMethods()) {
                    callback.method(o, method);
                }
            }
        });
    }

    public static void iterateAnnotation(final Object o, final Class<?> annoClass, final AnnotationFoundCallback callback)
 {
        iterateAnnotatedFields(o, annoClass, callback);
        iterateAnnotatedMethods(o, annoClass, callback);
    }

    public static void iterateAnnotatedFields(final Object o, final Class<?> annoClass, final AnnotatedFieldCallback callback)
 {
        iterateAnnotatedFields(o.getClass(), o, annoClass, callback);
    }

    public static void iterateAnnotatedMethods(final Object o, final Class<?> annoClass, final AnnotatedMethodCallback callback)
 {
        iterateAnnotatedMethods(o.getClass(), o, annoClass, callback);
    }

    public static void iterateAnnotatedFields(final Object o, final Class<?> annoClass, final Class<?> fieldType, final AnnotatedFieldCallback callback)
 {
        iterateAnnotatedFields(o.getClass(), o, annoClass, fieldType, callback);
    }

    private static void iterateAnnotatedFields(final Class<?> clazz, final Object o, final Class<?> annoClass, final AnnotatedFieldCallback callback)
 {
        iterateClassTree(clazz, new ClassCallback() {
            public void classFound(Class<?> cls) {
                for (Field field : cls.getDeclaredFields()) {
                    for (Annotation fieldAnnot : field.getAnnotations()) {
                        if (fieldAnnot.annotationType().equals(annoClass)) {
                            try {
                                callback.field(o, field);
                                break;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });
    }

    private static void iterateAnnotatedFields(final Class<?> clazz, final Object o, final Class<?> annoClass, final Class<?> fieldType, final AnnotatedFieldCallback callback)
 {
        iterateClassTree(clazz, new ClassCallback() {
            public void classFound(Class<?> cls) {
                for (Field field : cls.getDeclaredFields()) {
                    Class<?> type = field.getType();
                    if (!fieldType.isAssignableFrom(type)) {
                        continue;
                    }
                    for (Annotation fieldAnnot : field.getAnnotations()) {
                        if (!fieldAnnot.equals(annoClass)) {
                            continue;
                        }
                    }
                    try {
                        callback.field(o, field);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private static void iterateAnnotatedMethods(final Class<?> clazz, final Object o, final Class<?> annoClass, final AnnotatedMethodCallback callback)
 {
        iterateClassTree(clazz, new ClassCallback() {
            public void classFound(Class<?> cls) {
                for (Method method : cls.getDeclaredMethods()) {
                    for (Annotation methodAnnot : method.getAnnotations()) {
                        if (methodAnnot.annotationType().equals(annoClass)) {
                            callback.method(o, method);
                            break;
                        }
                    }
                }
            }
        });
    }

    public static void setAnnotatedFields(final Object o, final Class<?> annoClass, final Object fieldValue)
 {
        iterateAnnotatedFields(o, annoClass, new AnnotatedFieldCallback() {
            public void field(Object obj, Field field) {
                try {
                    field.setAccessible(true);
                    field.set(obj, fieldValue);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void setAnnotatedFields(final Object o, final Class<?> annoClass, final Class<?> fieldType, final Object fieldValue)
 {
        iterateAnnotatedFields(o, annoClass, fieldType, new AnnotatedFieldCallback() {
            public void field(Object obj, Field field) {
                try {
                    field.setAccessible(true);
                    field.set(obj, fieldValue);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static Class getParameterizedType(Class clazz) {
        return getParameterizedType(clazz, 0);
    }

    public static Class getParameterizedType(Class clazz, int index) {
        ParameterizedType s = (ParameterizedType) clazz.getGenericSuperclass();
        return (Class) s.getActualTypeArguments()[index];
    }

    public static Class getFieldGenericType(Class c, String fieldName) {
        try {
            Field f = c.getDeclaredField(fieldName);
            return getFieldGenericType(f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class getFieldGenericType(Field f) throws Exception {
        ParameterizedType gt = (ParameterizedType) f.getGenericType();
        return (Class) gt.getActualTypeArguments()[0];

    }

    public static Class getFieldGenericType(Field f, int index) throws Exception {
        ParameterizedType gt = (ParameterizedType) f.getGenericType();
        return (Class) gt.getActualTypeArguments()[index];
    }

    public static Method getMethod(Class cls, final String name, final Class<?>... parmTypes)
            throws Exception {
        final ArrayList<Method> holder = new ArrayList<Method>();
        iterateClassTree(cls, new ClassCallback() {

            public void classFound(Class<?> clazz) {
                try {
                    Method method = clazz.getMethod(name, parmTypes);
                    holder.add(method);
                } catch (Exception e) {
                    // don't remove try...catch
                }
            }
        });

        if(holder.size() == 0) {
            return null;
        }
        return holder.get(0);
    }

    public static boolean annotatedWith(Method method, Class annoCls) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation a : annotations) {
            if (a.annotationType().equals(annoCls))
                return true;
        }
        return false;
    }

    public static boolean annotatedWith(Field field, Class annoCls) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation a : annotations) {
            if (a.annotationType().equals(annoCls))
                return true;
        }
        return false;
    }

    public static Annotation getAnnotation(Field field, Class annoCls) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation a : annotations) {
            if (a.annotationType().equals(annoCls))
                return a;
        }
        return null;
    }

    public static boolean annotatedWith(Class<?> target, Class annoCls) {
        Annotation[] annotations = target.getAnnotations();
        for (Annotation a : annotations) {
            if (a.annotationType().equals(annoCls))
                return true;
        }
        return false;
    }

    public static Field getField(Class clazz, final String fieldName) {
        final ArrayList<Field> holder = new ArrayList<Field>();
        iterateClassTree(clazz, new ClassCallback() {

            public void classFound(Class<?> clazz) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    holder.add(field);
                } catch (Exception e) {
                    // don't remove this try...catch block.
                }
            }
        });
        if(holder.size() == 0) {
            return null;
        }
        return holder.get(0);
    }

    public static Method getMethod(Class clazz, final String methodName, final Class<?> parameterTypes) throws Exception {
        final ArrayList<Method> holder = new ArrayList<Method>();
        iterateClassTree(clazz, new ClassCallback() {
            public void classFound(Class<?> clazz) {
                try {
                    Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                    holder.add(method);
                } catch (Exception e) {
                    // don't remove this try...catch block.
                }
            }
        });
        if(holder.size() == 0) {
            return null;
        }
        return holder.get(0);
    }

    public static Method getSetter(Class clazz, final String fieldName, final Class<?> fieldType) throws Exception {
        final String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        return getMethod(clazz, methodName, fieldType);
    }

    public static Method getGetter(Class clazz, final String fieldName) throws Exception {
        try {
            String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            return getMethod(clazz, methodName, new Class<?>[0]);
        } catch (Exception e) {
            String methodName = "is" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            return getMethod(clazz, methodName, new Class<?>[0]);
        }
    }

    public static void setField(Object target, String fieldName, Object fieldValue) throws Exception {
        Field field = getField(target.getClass(), fieldName);
        field.set(target, fieldValue);
    }

    public static void callSetter(Object target, String fieldName, Object fieldValue) throws Exception {
        Method setter = getSetter(target.getClass(), fieldName, fieldValue.getClass());
        setter.invoke(target, fieldValue);
    }

    public static void callSetter(Object target, String fieldName, Object fieldValue, Class<?> fieldType) throws Exception {
        Method setter = getSetter(target.getClass(), fieldName, fieldType);
        setter.invoke(target, fieldValue);
    }

    public static void callSetter(Object target, String fieldName, String fieldValue) throws Exception {
        Field field = getField(target.getClass(), fieldName);
        if(field == null) {
        	return;
        }
        Class<?> fieldType = field.getType();
        if(isPrimeType(fieldType)) {
            Object value = convert(fieldValue, fieldType);
            callSetter(target, fieldName, value, fieldType);
        }
        else if(fieldType.isEnum()) {
            for(Object o : fieldType.getEnumConstants()){
                Enum<?> c = (Enum<?>) o;
                if(c.name().equals(fieldValue)){
                    callSetter(target, fieldName, c);
                }
            }
        } else {
            Method fromString = fieldType.getDeclaredMethod("fromString", String.class);
            Object value = fromString.invoke(fieldType.newInstance(), fieldValue);
            callSetter(target, fieldName, value);
        }
    }

    public static Object callGetter(Object target, String fieldName) throws Exception {
        Method getter = getGetter(target.getClass(), fieldName);
        return getter.invoke(target, new Object[0]);
    }

    public static boolean isPrimeType(Class type) {
        if(type.equals(String.class)) {
            return true;
        }
        if(type.equals(Integer.class) || type.equals(int.class)) {
            return true;
        }
        if(type.equals(Float.class) || type.equals(float.class)) {
            return true;
        }
        if(type.equals(Double.class) || type.equals(double.class)) {
            return true;
        }
        if(type.equals(Long.class) || type.equals(long.class)) {
            return true;
        }
        if(type.equals(Boolean.class) || type.equals(boolean.class)) {
            return true;
        }
        if(type.equals(Date.class)) {
            return true;
        }
        if (type.equals(Calendar.class)) {
            return true;
        }
        if (type.isEnum()) {
            return true;
        }

        return false;
    }

    public static Object convert(String value, Class toType) throws Exception {
        if(value == null) {
            return null;
        }
        if(toType.equals(String.class)) {
            return value;
        }
        if(toType.equals(Integer.class) || toType.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if(toType.equals(Float.class) || toType.equals(float.class)) {
            return Float.parseFloat(value);
        }
        if(toType.equals(Double.class) || toType.equals(double.class)) {
            return Double.parseDouble(value);
        }
        if(toType.equals(Long.class) || toType.equals(long.class)) {
            return Long.parseLong(value);
        }
        if(toType.equals(Boolean.class) || toType.equals(boolean.class)) {
            value = value.toLowerCase();
            if(value.equals("1") || value.startsWith("t") || value.startsWith("y") || value.equalsIgnoreCase("on")) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
        if(toType.equals(Date.class)) {
            SimpleDateFormat sdf = DateUtil.getIso8601DateFormat();
            return sdf.parse(value);
        }
        if (toType.equals(Calendar.class)) {
            SimpleDateFormat sdf = DateUtil.getIso8601DateFormat();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(sdf.parse(value));
            return calendar;
        }
        if (toType.isEnum()) {
            for (Object e : toType.getEnumConstants()) {
                if (e.toString().equals(value))
                    return e;
            }
        }
        throw new Exception("Unhandled data type: " + toType);
    }

    public static SimpleDateFormat getIso8601DateFormat() {
        final SimpleDateFormat ISO8601UTC = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ");// 24
        // characters
        ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC == GMT
        return ISO8601UTC;
    }

    public static boolean isPrimeType(Object item) {
        return isPrimeType(item.getClass());
    }

    public static void setFieldValue(Object target, Field field, Object value) {
        try {
            field.setAccessible(true);
            if (value == null) {
                field.set(target, null);
            } else if (field.getType().isAssignableFrom(value.getClass())) {
                field.set(target, value);
            } else if (isPrimeField(field)) {
                Object convert = convert(value.toString(), field.getType());
                field.set(target, convert);
            } else {
                throw new Exception("cannot set field value " + field.getName() + " for " + target.getClass());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String primeString(Object primeObject) {
        if (primeObject == null)
            return null;
        if (primeObject instanceof Date) {
            return getIso8601DateFormat().format((Date) primeObject);
        }
        if (primeObject instanceof Calendar) {
            return getIso8601DateFormat().format(((Calendar) primeObject).getTime());
        }
        return primeObject.toString();
    }

    public static boolean isPrimeField(Field field) {
        return isPrimeType(field.getType());
    }
}
