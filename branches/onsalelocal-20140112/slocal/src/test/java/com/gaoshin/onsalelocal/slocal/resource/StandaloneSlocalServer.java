package com.gaoshin.onsalelocal.slocal.resource;

import common.util.web.EmbeddedJetty;
import common.util.web.JettyWebAppContext;

public class StandaloneSlocalServer {
    public static void main(String[] args) throws Exception {
        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.setPort(1111);
        JettyWebAppContext jwac = new JettyWebAppContext();
        jwac.setContextPath("/slocal");
        jwac.setExtraClassPath("./src/main/resources/localmysql,./src/main/resources/shared");
        jetty.addWebAppContext(jwac.getWebAppContext());
        jetty.start();
        jetty.join();
    }
}
