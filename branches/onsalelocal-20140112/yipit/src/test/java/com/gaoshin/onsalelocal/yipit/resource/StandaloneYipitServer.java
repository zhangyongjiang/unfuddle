package com.gaoshin.onsalelocal.yipit.resource;

import common.util.web.EmbeddedJetty;
import common.util.web.JettyWebAppContext;

public class StandaloneYipitServer {
    public static void main(String[] args) throws Exception {
        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.setPort(1111);
        JettyWebAppContext jwac = new JettyWebAppContext();
        jwac.setContextPath("/yipit");
        jwac.setExtraClassPath("./src/main/resources/localmysql,./src/main/resources/shared");
        jetty.addWebAppContext(jwac.getWebAppContext());
        jetty.start();
        jetty.join();
    }
}
