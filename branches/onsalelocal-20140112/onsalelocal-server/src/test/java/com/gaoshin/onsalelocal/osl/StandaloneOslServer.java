package com.gaoshin.onsalelocal.osl;

import common.util.web.EmbeddedJetty;
import common.util.web.JettyWebAppContext;

public class StandaloneOslServer {
    public static void main(String[] args) throws Exception {
        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.setPort(9080);
        JettyWebAppContext jwac = new JettyWebAppContext();
        jwac.setContextPath("/osl2");
        jwac.setExtraClassPath("./src/main/resources/tunnel,./src/main/resources/shared");
        jetty.addWebAppContext(jwac.getWebAppContext());
        jetty.start();
        jetty.join();
    }
}
