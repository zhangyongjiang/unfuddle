package com.gaoshin.cloud.web.xen.bean;

import java.net.MalformedURLException;
import java.net.URL;

import com.xensource.xenapi.APIVersion;
import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Session;

public class XenSession {
    private Host host;
    private Connection connection;
    private Session session;
    private long lastAccessTime;

    public XenSession(Host host) {
        this.host = host;
    }
    
    public void open() {
        if(connection == null) {
            try {
                connection = new Connection(new URL(host.getUrl()));
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        
        if(session == null) {
            try {
                session = Session.loginWithPassword(connection, host.getUser(), host.getPassword(), APIVersion.API_1_5.toString());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void close() {
        try {
            session.logout(connection);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        session = null;
    }
    
    public String getSessionReference() {
        return connection.getSessionReference();
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
    
    public Connection getConnection() {
        return connection;
    }
}
