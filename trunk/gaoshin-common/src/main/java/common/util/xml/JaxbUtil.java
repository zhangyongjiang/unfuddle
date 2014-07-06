package common.util.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import common.util.reflection.AnnotationFoundCallback;
import common.util.reflection.ReflectionUtil;

public class JaxbUtil {

    private static HashMap<Class<?>, JAXBContext> jaxbContextMap = new HashMap<Class<?>, JAXBContext>();

    public static JAXBContext getJAXBContext(Class<?> cls) throws JAXBException {
        if (!jaxbContextMap.containsKey(cls)) {
            JAXBContext ctx = JAXBContext.newInstance(cls);
            jaxbContextMap.put(cls, ctx);
        }
        JAXBContext ctx = jaxbContextMap.get(cls);
        return ctx;
    }

    public static String getXmlString(Object obj) throws JAXBException {
        StringWriter sw = new StringWriter();
        Marshaller marshaller = getJAXBContext(obj.getClass()).createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    public static <T> T getObject(Class<T> cls, String xml) throws JAXBException {
        StringReader sr = new StringReader(xml);
        return (T) getJAXBContext(cls).createUnmarshaller().unmarshal(sr);
    }

    public static Document getDocument(Object obj) throws JAXBException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        getJAXBContext(obj.getClass()).createMarshaller().marshal(obj, doc);
        return doc;
    }

    public static HashMap<String, Node> getDocuments(Object... objs) throws JAXBException,
            ParserConfigurationException {
        HashMap<String, Node> ret = new HashMap<String, Node>();
        for (Object o : objs) {
            ret.put(o.getClass().getSimpleName(), getDocument(o));
        }
        return ret;
    }

    private static void copy(String fieldName, Class<?> type, Object src, Object dst, Class<?> expectedProfile, XmlProfile annotatedProfile)
            throws Exception {
        Field field = ReflectionUtil.getField(dst.getClass(), fieldName);
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method setter = dst.getClass().getMethod(setterName, type);
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if(field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
            getterName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        Method getter = src.getClass().getMethod(getterName, null);
        if(field == null) {
            return;
        }

        if (annotatedProfile != null) {
            boolean found = false;
            for (Class<?> s : annotatedProfile.value()) {
                if (expectedProfile.equals(s)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
//                Object[] args = new Object[1];
//                setter.invoke(dst, args);
                return;
            }
        }

        if (ReflectionUtil.annotatedWith(type, XmlType.class)) {
            Object subSrc = getter.invoke(src, null);
            if (subSrc == null) {
                return;
            }
            Object subDst = type.newInstance();
            setter.invoke(dst, subDst);
            copy(subSrc, subDst, expectedProfile);
            return;
        }

        Object value = getter.invoke(src, null);

        if (value instanceof List) {
            Class<?> itemType = ReflectionUtil.getFieldGenericType(field);
            List listSrc = (List) value;
            List listDst = new ArrayList();
            setter.invoke(dst, listDst);
            for (Object srcItem : listSrc) {
                Object dstItem = itemType.newInstance();
                listDst.add(dstItem);
                copy(srcItem, dstItem, expectedProfile);
            }
        } else {
            setter.invoke(dst, value);
        }
    }

    public static void copy(final Object src, final Object dst, final Class<?> profile) {
        try {
            ReflectionUtil.iterateAnnotation(dst, XmlElement.class, new AnnotationFoundCallback() {

                public void field(Object obj, Field field) {
                    try {
                        copy(field.getName(), field.getType(), src, obj, profile, field.getAnnotation(XmlProfile.class));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                public void method(Object obj, Method method) {
                    String getterName = method.getName();
                    String fieldName = getterName.substring(3);
                    if(getterName.startsWith("is")) {
                        fieldName = getterName.substring(2);
                    }
                    fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                    Class<?> retType = method.getReturnType();
                    try {
                        copy(fieldName, retType, src, obj, profile, method.getAnnotation(XmlProfile.class));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            ReflectionUtil.iterateAnnotation(dst, XmlAttribute.class, new AnnotationFoundCallback() {

                public void field(Object obj, Field field) {
                    try {
                        copy(field.getName(), field.getType(), src, obj, profile, field.getAnnotation(XmlProfile.class));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                public void method(Object obj, Method method) {
                    String getterName = method.getName();
                    String fieldName = getterName.substring(3);
                    fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                    Class<?> retType = method.getReturnType();
                    try {
                        copy(fieldName, retType, src, obj, profile, method.getAnnotation(XmlProfile.class));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
