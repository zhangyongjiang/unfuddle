package common.util.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.jersey.api.core.HttpContext;
import common.util.JacksonUtil;

@Provider
@Produces({"text/html;charset=UTF-8"})
public class HtmlMessageBodyWriter implements MessageBodyWriter<Object> {
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
        String format = requestInvoker.get().getParameter("format");
        boolean jsonOrXml = ((format != null && format.equalsIgnoreCase("xml")) || (format != null && format.equalsIgnoreCase("json")));
        return (mediaType.getSubtype().toLowerCase().indexOf("html")!=-1) || jsonOrXml;
    }

    public void writeTo(Object model, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4, MultivaluedMap<String, Object> arg5, OutputStream arg6)
            throws IOException, WebApplicationException {

        String format = requestInvoker.get().getParameter("format");
        if (format != null && format.equalsIgnoreCase("xml")) {
        	try {
				JAXBContext jc = JAXBContext.newInstance(model.getClass());
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty("jaxb.formatted.output",new Boolean(true)); 
				responseInvoker.get().setContentType("application/xml;charset=UTF-8");
				StringWriter sw = new StringWriter();
				marshaller.marshal(model, sw);
				arg6.write((sw.toString().replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br/>\n")).getBytes());
			} catch (JAXBException e) {
				throw new WebApplicationException(e);
			}
            return;
        }
        
        if (format != null && format.equalsIgnoreCase("object")) {
            String var = requestInvoker.get().getParameter("var");
            if(var == null || var.trim().length() == 0)
                var = "it";
            HttpServletRequest request = requestInvoker.get();
            request.setAttribute(var, model);
            return;
        }
        
    	try {
    		String json = JacksonUtil.obj2Json(model, true);
            responseInvoker.get().setContentType("application/json");
            responseInvoker.get().getWriter().write(json);
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
        return;
        
    }
}
