package com.gaoshin.cloud.web.xen.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XenConsole {
    private Long hostId;
    private String vmId;
    private String uuid;
    private String location;

    public String getVmId() {
        return vmId;
    }

    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String consoleLocation) {
        this.location = consoleLocation;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
