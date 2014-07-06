package com.gaoshin.cloud.web.xen.service;

import com.gaoshin.cloud.web.xen.bean.XenConsole;

public interface SessionManager {

    XenConsole getConsole(Long hostId, String vmId);

}
