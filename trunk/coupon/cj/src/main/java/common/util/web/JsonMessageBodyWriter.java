package common.util.web;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.sun.jersey.api.core.HttpContext;

//@Provider
//@Produces({"application/json;charset=UTF-8"})
public class JsonMessageBodyWriter implements MessageBodyWriter<Object> {
    @Context
    private HttpContext hc;

    @Context
    private ServletContext servletContext;

    @Context
    private ThreadLocal<HttpServletRequest> requestInvoker;

    @Context
    private ThreadLocal<HttpServletResponse> responseInvoker;

    public long getSize(Object arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4) {
        return -1;
    }

    public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType mediaType) {
    	return mediaType.getType().equalsIgnoreCase("application") && mediaType.getSubtype().equalsIgnoreCase("json");
    }

    public void writeTo(Object model, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4, MultivaluedMap<String, Object> arg5, OutputStream outputStream)
            throws IOException, WebApplicationException {
        ObjectMapper jsonProcessor = new ObjectMapper();
        if("1".equals(requestInvoker.get().getParameter("indent")))
            jsonProcessor.configure(Feature.INDENT_OUTPUT, true); 
		jsonProcessor.configure(Feature.WRITE_NULL_PROPERTIES, false);
		jsonProcessor.configure(
				org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		jsonProcessor.writeValue(outputStream, model);
    }
}
