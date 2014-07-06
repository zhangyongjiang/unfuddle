package com.gaoshin.cloud.web.xen.bean;

public class StorageRepoDetails extends StorageRepo {
    private Host host;
    private VirtualDiskImageList vdiList = new VirtualDiskImageList();

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public VirtualDiskImageList getVdiList() {
        return vdiList;
    }

    public void setVdiList(VirtualDiskImageList vdiList) {
        this.vdiList = vdiList;
    }
}
