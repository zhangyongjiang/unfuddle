package com.gaoshin.cloud.web.vm.entity;

public enum ResourceState {
    Creating,
    Enabled,
    Disabled,
    Unmanaged,
    PrepareForMaintenance,
    ErrorInMaintenance,
    Maintenance,
    Error;
}
