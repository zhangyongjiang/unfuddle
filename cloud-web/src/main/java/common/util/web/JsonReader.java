package common.util.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.sun.jersey.api.core.HttpContext;

//@Provider
//@Consumes("application/json")
public class JsonReader implements MessageBodyReader {

    private @Context
    HttpContext hc;
    private @Context
    ServletContext servletContext;
    private @Context
    ThreadLocal<HttpServletRequest> requestInvoker;
    private @Context
    ThreadLocal<HttpServletResponse> responseInvoker;

    public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    	return mediaType.getType().equalsIgnoreCase("application") && mediaType.getSubtype().equalsIgnoreCase("json");
    }

    public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            String data = IOUtils.toString(entityStream);
            StringReader sr = new StringReader(data);
            ObjectMapper jsonProcessor = new ObjectMapper();
    		jsonProcessor.configure(Feature.WRITE_NULL_PROPERTIES, false);
    		jsonProcessor.configure(
    				org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
    				false);
    		return jsonProcessor.readValue(sr, type);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }

}
