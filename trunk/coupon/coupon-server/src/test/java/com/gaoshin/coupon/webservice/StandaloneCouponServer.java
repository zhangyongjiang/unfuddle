package com.gaoshin.coupon.webservice;

import common.util.web.EmbeddedJetty;
import common.util.web.JettyWebAppContext;

public class StandaloneCouponServer {
    public static void main(String[] args) throws Exception {
        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.setPort(8080);
        JettyWebAppContext jwac = new JettyWebAppContext();
        jwac.setContextPath("/coupon");
        jwac.setExtraClassPath("./src/main/resources/localmysql,./src/main/resources/shared,../coupon-crawler/main/resources");
        jetty.addWebAppContext(jwac.getWebAppContext());
        jetty.start();
        jetty.join();
    }
}
