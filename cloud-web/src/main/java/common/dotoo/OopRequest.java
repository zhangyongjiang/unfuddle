package common.dotoo;

import java.util.ArrayList;

import javax.servlet.ServletContext;

public class OopRequest {

    private String uri;
    private String param;
    private String oopContext;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public void setOopContext(String oopContext) {
        this.oopContext = oopContext;
    }

    public String getOopContext() {
        return oopContext;
    }

    /**
     * Return "path" if uri is "path/file.jsp.oo"
     */
    public String getPath() {
        int pos = uri.lastIndexOf('/');
        return uri.substring(0, pos);
    }

    /**
     * Return "path/file.jsp" if uri is "path/file.jsp.oo"
     */
    public String getSearchUri() {
        int pos = uri.lastIndexOf('.');
        return uri.substring(0, pos);
    }

    /**
     * Return "file.jsp.oo" if uri is "path/file.jsp.oo"
     */
    public String getFile() {
        int pos = uri.lastIndexOf('/');
        return uri.substring(pos + 1);
    }

    /**
     * Return "file.jsp" if uri is "path/file.jsp.oo"
     */
    public String getRealFile() {
        String file = getFile();
        int pos = file.lastIndexOf('.');
        return file.substring(0, pos);
    }

    public String appendParam(String pathToFile) {
        if (param == null) {
            return pathToFile;
        } else {
            return pathToFile + "?" + param;
        }
    }

    /**
     * return bottom up path list based on the context path
     */
    private ArrayList<String> expandSearchUrl(String searchFile) {
        ArrayList<String> result = new ArrayList<String>();
        result.add("/" + searchFile);

        String searchUri = getOopContext();
        String[] dirs = searchUri.split("/");
        String prev = "";
        for (String s : dirs) {
            if(s.length() ==0) {
                continue;
            }
            int pos = s.indexOf(';');
            if(pos != -1) {
            	s = s.substring(0, pos);
            }
            pos = s.indexOf("--");
            if(pos != -1) {
            	s = s.substring(0, pos);
            }
            pos = s.indexOf(":");
            if(pos != -1) {
            	s = s.substring(0, pos);
            }
            prev = prev + "/" + s;
            result.add(prev + "/" + searchFile);
        }

        ArrayList<String> reversed = new ArrayList<String>();
        for(int i=result.size()-1; i>=0; i--) {
            reversed.add(result.get(i));
        }

        return reversed;
    }

    private String searchResource(ServletContext servletContext, String searchFile) throws Exception {
        ArrayList<String> searchList = expandSearchUrl(searchFile);
        String contextPath = servletContext.getContextPath();
        for (String url : searchList) {
            if(url.startsWith(contextPath)) {
                url = url.substring(contextPath.length());
            }
            if (servletContext.getResource(url) != null) {
                return url;
            }
        }
        return null;
    }

    public String searchResource(ServletContext servletContext) throws Exception {
        String resourcePath = searchResource(servletContext, getRealFile());
        return resourcePath;
    }
}
