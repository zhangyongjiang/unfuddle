package com.gaoshin.cloud.web.xen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VmDetails extends Vm {
    private Host host;
    private String ipAddress;
    private List<XenConsole> consoleList = new ArrayList<XenConsole>();
    private List<VirtualBlockDevice> vbdList;
    private List<VirtualNetworkInterface> vifList;

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void setIpAddress(String ip) {
        ipAddress = ip;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }

    public List<XenConsole> getConsoleList() {
        return consoleList;
    }

    public void setConsoleList(List<XenConsole> consoleList) {
        this.consoleList = consoleList;
    }

    public List<VirtualBlockDevice> getVbdList() {
        return vbdList;
    }

    public void setVbdList(List<VirtualBlockDevice> vbdList) {
        this.vbdList = vbdList;
    }

    public void setVirtualNetworkInterfaceList(List<VirtualNetworkInterface> items) {
        this.setVifList(items);
    }

    public List<VirtualNetworkInterface> getVifList() {
        return vifList;
    }

    public void setVifList(List<VirtualNetworkInterface> vifList) {
        this.vifList = vifList;
    }

}
