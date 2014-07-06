package com.gaoshin.onsalelocal.osl;

import common.util.web.EmbeddedJetty;
import common.util.web.JettyWebAppContext;

public class StandaloneOslServer {
    public static void main(String[] args) throws Exception {
        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.setPort(2222);
        JettyWebAppContext jwac = new JettyWebAppContext();
        jwac.setContextPath("/osl");
        jwac.setExtraClassPath("./src/test/resources,./src/main/resources/prod,./src/main/resources/shared");
        jetty.addWebAppContext(jwac.getWebAppContext());
        jetty.start();
        jetty.join();
    }
}
