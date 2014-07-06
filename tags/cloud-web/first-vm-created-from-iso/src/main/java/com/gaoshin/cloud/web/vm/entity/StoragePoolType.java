package com.gaoshin.cloud.web.vm.entity;

public enum StoragePoolType {
    Filesystem(false), //local directory
    NetworkFilesystem(true), //NFS or CIFS
    IscsiLUN(true), //shared LUN, with a clusterfs overlay
    Iscsi(true), //for e.g., ZFS Comstar
    ISO(false),    // for iso image
    LVM(false),    // XenServer local LVM SR
    CLVM(true),
    SharedMountPoint(true),
    VMFS(true),     // VMware VMFS storage
    PreSetup(true),  // for XenServer, Storage Pool is set up by customers. 
    EXT(false),    // XenServer local EXT SR
    OCFS2(true);
    
    boolean shared;
    
    StoragePoolType(boolean shared) {
        this.shared = shared;
    }
    
    public boolean isShared() {
        return shared;
    }

}
