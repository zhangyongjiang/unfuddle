package com.gaoshin.cloud.web.xen.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.xensource.xenapi.Types.ConsoleProtocol;

@XmlRootElement
public class XenConsole {
    private Long hostId;
    private String vmId;
    private String uuid;
    private String location;
    private ConsoleProtocol protocol;

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

    public void setProtocol(ConsoleProtocol protocol) {
        this.protocol = protocol;
    }
    
    public ConsoleProtocol getProtocol() {
        return protocol;
    }
}
