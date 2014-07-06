package common.dotoo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * request must be with format ...path-to-file-plus.jsp.oo
 */
public class OopServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            OopRequest oopRequest = RequestChain.begin(request);
            String url = oopRequest.searchResource(getServletContext());
            if(url == null) {
                response.getWriter().write("!!!!!!!! cannot find " + oopRequest.getUri() + "?" + oopRequest.getParam());
            }
            else {
                getServletContext().getRequestDispatcher(oopRequest.appendParam(url)).include(request, response);
            }
        } catch (Exception e) {
        	e.printStackTrace(System.out);
            throw new ServletException(e);
        } finally {
            RequestChain.end(request);
        }
    }
}
