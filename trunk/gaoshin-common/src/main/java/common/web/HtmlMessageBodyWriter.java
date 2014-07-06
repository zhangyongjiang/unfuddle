package common.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
@Produces({ "text/html;charset=UTF-8" })
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

    public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
        return true;
    }

    public void writeTo(Object model, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4,
            MultivaluedMap<String, Object> arg5, OutputStream arg6)
            throws IOException, WebApplicationException {
        String modelClass = model.getClass().getName();

        String format = requestInvoker.get().getParameter("format");
        if (format != null && format.equalsIgnoreCase("xml")) {
            try {
                JAXBContext jc = JAXBContext.newInstance(model.getClass());
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
                responseInvoker.get().setContentType("application/xml;charset=UTF-8");
                StringWriter sw = new StringWriter();
                marshaller.marshal(model, sw);
                arg6.write((sw.toString().replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br/>\n")).getBytes());
            } catch (JAXBException e) {
                throw new WebApplicationException(e);
            }
            return;
        }

        if (format != null && format.equalsIgnoreCase("json")) {
            try {
                String json = JacksonUtil.obj2Json(model, true);
                responseInvoker.get().getWriter().write(json);
            } catch (Exception e) {
                throw new WebApplicationException(e);
            }
            return;
        }

        HttpServletRequest request = requestInvoker.get();
        HttpServletResponse response = responseInvoker.get();

        if (format != null && format.equalsIgnoreCase("object")) {
            String var = requestInvoker.get().getParameter("var");
            if (var != null) {
                request.setAttribute(var, model);
            } else {
                request.setAttribute("it", model);
            }
            return;
        }

        Object oldIt = requestInvoker.get().getAttribute("it");
        Object oldModel = request.getAttribute(modelClass);
        String contextPath = servletContext.getContextPath();

        String viewRoot = getViewRoot();
        try {
            request.setAttribute(modelClass, model);
            request.setAttribute("it", model);

            String uri = request.getParameter("uri");
            if (uri == null) {
                uri = (String) request.getAttribute("javax.servlet.include.request_uri");
            }
            if (uri == null) {
                uri = request.getRequestURI();
            }

            if (request.getMethod().equalsIgnoreCase("post")) {
                uri = uri + "/post";
            }

            String oopath = null;
            String view = request.getParameter("view");
            if ((view != null) && (view.length() > 0)) {
                if (view.endsWith(".jsp.oo")) {
                    oopath = view;
                } else if (view.endsWith(".jsp")) {
                    oopath = view + ".oo";
                } else {
                    oopath = uri + "/" + model.getClass().getSimpleName() + "-" + view + ".jsp.oo";
                    oopath = oopath.substring(0, oopath.indexOf(contextPath) + contextPath.length()) + "/" + viewRoot + oopath.substring(oopath.indexOf(contextPath) + contextPath.length());
                }
            } else {
                oopath = uri + "/" + model.getClass().getSimpleName() + ".jsp.oo";
                oopath = oopath.substring(0, oopath.indexOf(contextPath) + contextPath.length()) + "/" + viewRoot + oopath.substring(oopath.indexOf(contextPath) + contextPath.length());
            }

            servletContext.getRequestDispatcher(oopath).include(request, response);
        } catch (ServletException e) {
            throw new WebApplicationException(e);
        } finally {
            request.setAttribute("it", oldIt);
            request.setAttribute(modelClass, oldModel);
        }
    }

    private HashMap<String, String> getCookieMap() {
        Cookie[] cookies = requestInvoker.get().getCookies();
        HashMap<String, String> cookieMap = new HashMap<String, String>();
        if (cookies != null) {
            for (Cookie c : cookies) {
                cookieMap.put(c.getName(), c.getValue());
            }
        }
        return cookieMap;
    }

    protected String getCookie(String name) {
        return getCookieMap().get(name);
    }

    private String getViewRoot() {
        HttpServletRequest request = requestInvoker.get();
        String viewRoot = (String) request.getParameter("VIEW_ROOT");
        if (viewRoot == null)
            viewRoot = getCookie("VIEW_ROOT");
        if (viewRoot == null)
            viewRoot = (String) request.getSession().getAttribute("VIEW_ROOT");

        if (viewRoot == null)
            viewRoot = servletContext.getInitParameter("VIEW_ROOT");
        if (viewRoot == null) {
            throw new WebApplicationException(new RuntimeException("cannot find configuration VIEW_ROOT. Please specify it in web.xml"));
        }
        return viewRoot;
    }
}
