package com.gaoshin.cloud.web.xen.bean;

public class ConsoleSession {
    private XenConsole console;
    private String sessionId;

    public XenConsole getConsole() {
        return console;
    }

    public void setConsole(XenConsole console) {
        this.console = console;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
