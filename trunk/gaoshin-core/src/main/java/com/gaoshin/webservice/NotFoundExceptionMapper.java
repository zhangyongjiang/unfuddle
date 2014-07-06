package com.gaoshin.webservice;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.core.HttpContext;

@Provider
@Produces({ "text/html;charset=utf-8", "text/html" })
public class NotFoundExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private HttpContext hc;
    @Context private ThreadLocal<HttpServletRequest> requestInvoker;
    @Context private ThreadLocal<HttpServletResponse> responseInvoker;
    @Context private ServletContext servletContext;
    
    public Response toResponse(WebApplicationException ex) {
            ex.printStackTrace();
		try {
            if (!hc.getRequest().getMethod().equalsIgnoreCase("get")) {
                return ex.getResponse();
            } else if (ex.getResponse().getStatus() != 404 && ex.getResponse().getStatus() != 405) {
                return ex.getResponse();
            } else {
                includeDotOO();
                return Response.ok().build();
            }
		} catch (Exception e) {
			return Response.serverError().build();
		}
    }

    protected void includeDotOO() throws IOException, ServletException {
        StringBuilder sb = new StringBuilder();
        MultivaluedMap<String, String> queryParameters = hc.getRequest().getQueryParameters();
        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        String contextPath = servletContext.getContextPath();

        String defaultView = (String) requestInvoker.get().getSession().getAttribute("VIEW_ROOT");
        if (defaultView == null)
            defaultView = servletContext.getInitParameter("VIEW_ROOT");
        if (defaultView == null) {
            throw new WebApplicationException(new RuntimeException("cannot find configuration VIEW_ROOT. Please specify it in web.xml"));
        }

        String uri = requestInvoker.get().getRequestURI();
        String oopath = uri + "/index.jsp.oo?" + sb.toString();

        oopath = oopath.substring(0, oopath.indexOf(contextPath) + contextPath.length()) + "/" + defaultView + oopath.substring(oopath.indexOf(contextPath) + contextPath.length());

        requestInvoker.get().getRequestDispatcher(oopath).include(requestInvoker.get(), responseInvoker.get());
    }
}
