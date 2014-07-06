package com.gaoshin.cloud.web.vm.entity;

public enum Type {
    Storage(false),
    Routing(false),
    SecondaryStorage(false),
    SecondaryStorageCmdExecutor(false),
    ConsoleProxy(true),
    ExternalFirewall(false),
    ExternalLoadBalancer(false),
    PxeServer(false),
    TrafficMonitor(false),

    ExternalDhcp(false),
    SecondaryStorageVM(true),
    LocalSecondaryStorage(false);
    boolean _virtual;
    private Type(boolean virtual) {
        _virtual = virtual;
    }

    public boolean isVirtual() {
        return _virtual;
    }

}
