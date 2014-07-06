package common.util.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.core.HttpContext;
import common.util.reflection.ReflectionUtil;

@Provider
@Consumes("application/x-www-form-urlencoded")
public class FormReader implements MessageBodyReader {

    private @Context
    HttpContext hc;
    private @Context
    ServletContext servletContext;
    private @Context
    ThreadLocal<HttpServletRequest> requestInvoker;
    private @Context
    ThreadLocal<HttpServletResponse> responseInvoker;

    public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return mediaType.equals(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    }

    public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            if(type.equals(InputStream.class)) {
                return entityStream;
            }
            Object bean = type.newInstance();
            String postData = IOUtils.toString(entityStream);
            if((postData == null) || (postData.length() == 0)) {
                postData = requestInvoker.get().getQueryString();
            }
            if(postData == null) {
                postData = "";
            }
            for(String s : postData.split("&")) {
                if(s.indexOf('=')==-1) {
                    continue;
                }
                String[] kv = s.split("=");
                if(kv.length<2) {
                    continue;
                }
                String key = URLDecoder.decode(kv[0].trim(), "UTF-8");
                String fieldValue = URLDecoder.decode(kv[1].trim(), "UTF-8");
                if(fieldValue.length() == 0) {
                    fieldValue = null;
                }
                if(bean instanceof HashMap) {
                    ((HashMap<String, String>)bean).put(key, fieldValue);
                }
                else {
                    ReflectionUtil.callSetter(bean, key, fieldValue);
                }
            }

            requestInvoker.get().setAttribute("it", bean);

//            Validator validator = new Validator();
//            List<ConstraintViolation> violations = validator.validate(bean);
//            if(violations.size() > 0) {
//                throw new InvalidInputListException(violations);
//            }
            
            return bean;
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }

}
