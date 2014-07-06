package com.gaoshin.cloud.web.xen.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VirtualNetworkInterface {
    private String uuid;
    private String statusDetail;
    private String mac;
    private String device;
    private Boolean currentlyAttached;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setCurrentlyAttached(Boolean currentlyAttached) {
        this.currentlyAttached = currentlyAttached;
    }

    public String getUuid() {
        return uuid;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public String getMac() {
        return mac;
    }

    public String getDevice() {
        return device;
    }

    public Boolean getCurrentlyAttached() {
        return currentlyAttached;
    }
}
