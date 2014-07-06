package common.util.web;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;

import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.Resource;

public class JettyWebAppContext {

    private String contextPath = "/"; // default root context path

    // if null, the web.xml in the resouceBase/WEB-INF/web.xml will be used
    private String webXml = null;

    // be default, we assume the current dir is the project dir
    private String resourceBase = "src/main/webapp";

    private String extraClassPath = "src/main/resources";

    private final ArrayList<EventListener> eventListenerList = new ArrayList<EventListener>();

    public JettyWebAppContext(String contextPath, String webXml, String resourceBase) {
        setContextPath(contextPath);
        setWebXml(webXml);
        setResourceBase(resourceBase);
    }

    public JettyWebAppContext(String contextPath, String resourceBase) {
        setContextPath(contextPath);
        setResourceBase(resourceBase);
    }

    public JettyWebAppContext(String resourceBase) {
        setResourceBase(resourceBase);
    }

    public JettyWebAppContext() {
    }

    public WebAppContext getWebAppContext() throws Exception {
        WebAppContext wac = new WebAppContext();
        wac.setContextPath(getContextPath());
        if (getWebXml() != null) {
            wac.setDescriptor(getWebXml());
        }

        File file = new File(getResourceBase());
        if (!file.exists()) {
            URL url = ClassLoader.getSystemResource(getResourceBase());
            if(url == null) {
                url = this.getClass().getClassLoader().getResource(getResourceBase());
            }
            if(url == null) {
                url = this.getClass().getClassLoader().getSystemResource(getResourceBase());
            }
            if (url != null) {
                wac.setBaseResource(Resource.newResource(url));
            } else {
                // assume it's in the classpath
                wac.setBaseResource(Resource.newClassPathResource(getResourceBase()));
            }
        } else {
            wac.setResourceBase(getResourceBase());
        }

        if (getExtraClassPath() != null) {
            wac.setExtraClasspath(getExtraClassPath());
        }

        if (eventListenerList.size() > 0) {
            for (EventListener el : eventListenerList) {
                wac.addEventListener(el);
            }
        }

        return wac;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getWebXml() {
        return webXml;
    }

    public void setWebXml(String webXml) {
        this.webXml = webXml;
    }

    public String getResourceBase() {
        return resourceBase;
    }

    public void setResourceBase(String resourceBase) {
        this.resourceBase = resourceBase;
    }

    public void setExtraClassPath(String extraClassPath) {
        this.extraClassPath = extraClassPath;
    }

    public String getExtraClassPath() {
        return extraClassPath;
    }

    public void addEventListener(EventListener eventListener) {
        eventListenerList.add(eventListener);
    }
}
