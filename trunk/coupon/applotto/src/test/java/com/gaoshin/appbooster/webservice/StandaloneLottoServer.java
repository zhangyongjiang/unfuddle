package com.gaoshin.appbooster.webservice;

import common.util.web.EmbeddedJetty;
import common.util.web.JettyWebAppContext;

public class StandaloneLottoServer {
    public static void main(String[] args) throws Exception {
        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.setPort(8888);
        JettyWebAppContext jwac = new JettyWebAppContext();
        jwac.setContextPath("/appbooster");
        jwac.setExtraClassPath("./src/main/resources/localmysql,./src/main/resources/shared");
        jetty.addWebAppContext(jwac.getWebAppContext());
        jetty.start();
        jetty.join();
    }
}
