package common.util.web;

import java.util.Random;

import javax.servlet.ServletContext;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.After;
import org.junit.Before;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class ResourceTester {
	private static final char[] Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	protected EmbeddedJetty server = null;
	protected final String contextPath = "/testcontext";
	protected WebResource resource = null;
	protected final String Password = "password";
	private int serverRefCount = 0;
    protected WebAppContext webAppContext;
	private Random random = new Random(System.currentTimeMillis());
	private ApacheHttpClient client;
    private WebApplicationContext webApplicationContext;
	
	@Before
	public synchronized void setup() throws Exception {
		server = new EmbeddedJetty();
		JettyWebAppContext jwac = new JettyWebAppContext();
		jwac.setResourceBase("./src/main/webapp");
		jwac.setContextPath(contextPath);
	    jwac.setExtraClassPath("./src/main/resources/shared");
		webAppContext = jwac.getWebAppContext();
        server.addWebAppContext(webAppContext);
		server.start();
		System.out.println(System.getenv());
		System.out.println("Started embedded Jetty. " + server);

		// Configure Logging
		String logging = "org.apache.commons.logging";
		System.setProperty(logging + ".Log", logging + ".impl.SimpleLog");
		System.setProperty(logging + ".logging.simplelog.showdatetime", "true");
		System.setProperty(logging + ".simplelog.log.httpclient.wire", "debug");
		System.setProperty(logging + ".simplelog.log.org.apache.commons.httpclient", "debug");

		DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
		config.getProperties().put(
				DefaultApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);

		config.getClasses().add(JacksonJsonProvider.class);
		
		client = ApacheHttpClient.create(config);
		String uri = server.getBaseUri(contextPath);
		resource = client.resource(uri);
		
		serverRefCount ++;
	}

	@After
	public synchronized void shutdown() throws Exception {
						server.stop();
	}

	protected void print(String msg) {
		System.out.println("====================" + msg);
	}
	
	protected WebResource getWebResource(String path) {
        WebResource webResource = resource.path(path);
        return webResource;
	}

	protected Builder getBuilder(String path, String... queryParameters) {
		WebResource webResource = getWebResource(path);
		if(queryParameters != null) {
			for(int i=0; i<queryParameters.length; i=i+2) {
				webResource = webResource.queryParam(queryParameters[i], queryParameters[i+1]);
			}
		}
		Builder builder = webResource.header("Content-type", "application/json;charset=utf-8").accept("application/json;charset=utf-8");
        return builder;
	}
	
	protected Builder getBuilder(String path) {
        WebResource webResource = getWebResource(path);
		Builder builder = webResource.header("Content-type", "application/json;charset=utf-8").accept("application/json;charset=utf-8");
        return builder;
	}
	
	protected void post(String path) {
		getBuilder(path).post(" ");
	}
	
	protected <T> T post(String path, Class<T> respClass) {
		return getBuilder(path).post(respClass, " ");
	}
	
	protected String getCurrentTimeMillisString() {
	    try {
            Thread.sleep(10);
        }
        catch (InterruptedException e) {
        }
		return System.currentTimeMillis() + "";
	}

	protected String getRandomString(int minLen, int maxLen) {
		int len = minLen + random.nextInt(maxLen - minLen);
		StringBuilder sb = new StringBuilder();
		while(len-->0) {
			sb.append(Chars[random.nextInt(Chars.length)]);
		}
		return sb.toString();
	}
	
    protected synchronized <T> T getSpringBean(Class<T> cls, String name) {
        if(webApplicationContext == null) {
            ServletContext context = webAppContext.getServletContext().getContext(contextPath);
            webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
        }
        return webApplicationContext.getBean(name, cls);
    }
	
	protected synchronized <T> T getSpringBean(Class<T> cls) {
	    if(webApplicationContext == null) {
    	    ServletContext context = webAppContext.getServletContext().getContext(contextPath);
    	    webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
	    }
	    return webApplicationContext.getBean(cls);
	}
}