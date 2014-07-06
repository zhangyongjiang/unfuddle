package com.gaoshin.cloud.web.xen.bean;

public class HostDetails extends Host {
    private VmList vmList = new VmList();
    private StorageRepoList srList = new StorageRepoList();
    private VirtualDiskImageList vdiList = new VirtualDiskImageList();

    public VmList getVmList() {
        return vmList;
    }

    public void setVmList(VmList vmList) {
        this.vmList = vmList;
    }

    public StorageRepoList getStorageRepoList() {
        return srList;
    }

    public void setStorageRepoList(StorageRepoList srList) {
        this.srList = srList;
    }

    public VirtualDiskImageList getVdiList() {
        return vdiList;
    }

    public void setVdiList(VirtualDiskImageList vdiList) {
        this.vdiList = vdiList;
    }
}
