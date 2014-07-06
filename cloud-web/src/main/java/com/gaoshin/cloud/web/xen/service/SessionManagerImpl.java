package com.gaoshin.cloud.web.xen.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.xen.bean.XenConsole;
import com.gaoshin.cloud.web.xen.dao.XenDao;
import com.xensource.xenapi.Connection;

@Service("sessionManager")
@Transactional
public class SessionManagerImpl implements SessionManager {
    @Autowired
    private XenDao xenDao;

    private Map<String, Connection> hostConnections = new HashMap<String, Connection>();

    @Override
    public XenConsole getConsole(Long hostId, String vmId) {
        // TODO Auto-generated method stub
        return null;
    }
}
