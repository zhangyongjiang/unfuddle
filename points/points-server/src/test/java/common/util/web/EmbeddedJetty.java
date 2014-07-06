package common.util.web;

import java.util.ArrayList;
import java.util.Random;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class EmbeddedJetty {

    private int port = 0;
    private Server server = null;
    private ArrayList<WebAppContext> listWebAppContext = new ArrayList<WebAppContext>();

    public EmbeddedJetty(int port) {
        setPort(port);
    }

    public EmbeddedJetty() {
    }

    public void start() throws Exception {
        server = new Server(getPort());
        for(WebAppContext wac : listWebAppContext) {
            server.addHandler(wac);
        }
        server.setStopAtShutdown(true);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public int getPort() {
        if (port <= 0) {
            port = new Random(System.nanoTime()).nextInt(1000) + 8000;
        }
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public static void main(String[] args) throws Exception {
        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.setPort(8080);
		JettyWebAppContext jwac = new JettyWebAppContext();
		jwac.setContextPath("/bdserver");
		jwac.setExtraClassPath("./src/test/resources");
		jetty.addWebAppContext(jwac.getWebAppContext());
        jetty.start();
        jetty.join();
    }

    public void addWebAppContext(WebAppContext wac) {
        listWebAppContext.add(wac);
    }

    public String getBaseUri() {
        return "http://localhost:" + getPort();
    }

    public String getBaseUri(String contextPath) {
        return "http://localhost:" + getPort() + contextPath;
    }
    
    public String toString() {
    	return getBaseUri();
    }
}
