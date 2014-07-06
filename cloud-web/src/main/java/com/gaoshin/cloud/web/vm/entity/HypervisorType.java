package com.gaoshin.cloud.web.vm.entity;

public enum HypervisorType {
    None, //for storage hosts
    XenServer,
    KVM,
    VMware,
    Hyperv,     
    VirtualBox,
    Parralels,
    BareMetal,
    Simulator,
    Ovm,
    Any;
}
