package com.gaoshin.cloud.web.xen.service;

import java.net.URL;

import com.gaoshin.cloud.web.xen.bean.Host;
import com.xensource.xenapi.APIVersion;
import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Session;

public class XenTask<T> {
    private String url;
    private String user;
    private String pwd;
    private T result;
    
    protected Connection connection;
    protected Session session;
    
    public XenTask(Host host) {
        this(host.getUrl(), host.getUser(), host.getPassword());
    }
    
    public XenTask(String url, String user, String pwd) {
        this.url = url;
        this.user = user;
        this.pwd = pwd;
    }
    
    protected void doTask() throws Exception {
    }
    
    public void exec() throws Exception {
        connection = new Connection(new URL(url));
        session = Session.loginWithPassword(connection, user, pwd, APIVersion.API_1_5.toString());
        doTask();
        session.logout(connection);
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
