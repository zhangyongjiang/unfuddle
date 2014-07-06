package com.gaoshin.cloud.web.xen.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.xensource.xenapi.Types.VbdType;

@XmlRootElement
public class VirtualBlockDevice {

    private Boolean bootable;
    private String uuid;
    private Boolean unpluggable;
    private VbdType type;
    private String statusDetail;
    private Boolean empty;
    private String device;
    private Boolean currentAttached;

    public Object getStatusDetail() {
        return statusDetail;
    }

    public Boolean getCurrentAttached() {
        return currentAttached;
    }

    public void setCurrentAttached(Boolean currentAttached) {
        this.currentAttached = currentAttached;
    }

    public Boolean getBootable() {
        return bootable;
    }

    public String getUuid() {
        return uuid;
    }

    public Boolean getUnpluggable() {
        return unpluggable;
    }

    public VbdType getType() {
        return type;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public String getDevice() {
        return device;
    }

    public void setBootable(Boolean bootable) {
        this.bootable = bootable;
    }

    public void setCurrentlyAttached(Boolean currentlyAttached) {
        this.currentAttached = currentlyAttached;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public void setType(VbdType type) {
        this.type = type;
    }

    public void setUnpluggable(Boolean unpluggable) {
        this.unpluggable = unpluggable;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
