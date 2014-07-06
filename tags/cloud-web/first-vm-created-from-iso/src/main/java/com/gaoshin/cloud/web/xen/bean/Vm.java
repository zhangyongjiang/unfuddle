package com.gaoshin.cloud.web.xen.bean;

import com.xensource.xenapi.Types;

public class Vm {
    private Long id;
    private Long hostId;
    private boolean template;
    private String uuid;
    private Types.VmPowerState powerState;
    private String nameLabel;
    private String nameDescription;
    private Long userVersion;
    private Long memoryTarget;
    private Long memoryStaticMax;
    private Long memoryDynamicMax;
    private Long memoryDynamicMin;
    private Long memoryStaticMin;
    private Long VCPUsMax;
    private Long VCPUsAtStartup;
    private String PVArgs;
    private Long snapshotTime;
    private Boolean isASnapshot;
    private Long domid;
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Types.VmPowerState getPowerState() {
        return powerState;
    }

    public void setPowerState(Types.VmPowerState powerState) {
        this.powerState = powerState;
    }

    public String getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel = nameLabel;
    }

    public String getNameDescription() {
        return nameDescription;
    }

    public void setNameDescription(String nameDescription) {
        this.nameDescription = nameDescription;
    }

    public Long getUserVersion() {
        return userVersion;
    }

    public void setUserVersion(Long userVersion) {
        this.userVersion = userVersion;
    }

    public Long getMemoryTarget() {
        return memoryTarget;
    }

    public void setMemoryTarget(Long memoryTarget) {
        this.memoryTarget = memoryTarget;
    }

    public Long getMemoryStaticMax() {
        return memoryStaticMax;
    }

    public void setMemoryStaticMax(Long memoryStaticMax) {
        this.memoryStaticMax = memoryStaticMax;
    }

    public Long getMemoryDynamicMax() {
        return memoryDynamicMax;
    }

    public void setMemoryDynamicMax(Long memoryDynamicMax) {
        this.memoryDynamicMax = memoryDynamicMax;
    }

    public Long getMemoryDynamicMin() {
        return memoryDynamicMin;
    }

    public void setMemoryDynamicMin(Long memoryDynamicMin) {
        this.memoryDynamicMin = memoryDynamicMin;
    }

    public Long getMemoryStaticMin() {
        return memoryStaticMin;
    }

    public void setMemoryStaticMin(Long memoryStaticMin) {
        this.memoryStaticMin = memoryStaticMin;
    }

    public Long getVCPUsMax() {
        return VCPUsMax;
    }

    public void setVCPUsMax(Long vCPUsMax) {
        VCPUsMax = vCPUsMax;
    }

    public Long getVCPUsAtStartup() {
        return VCPUsAtStartup;
    }

    public void setVCPUsAtStartup(Long vCPUsAtStartup) {
        VCPUsAtStartup = vCPUsAtStartup;
    }

    public String getPVArgs() {
        return PVArgs;
    }

    public void setPVArgs(String pVArgs) {
        PVArgs = pVArgs;
    }

    public Long getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(Long snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public void setSnapshort(Boolean isASnapshot) {
        this.setIsASnapshot(isASnapshot);
    }

    public Boolean getIsASnapshot() {
        return isASnapshot;
    }

    public void setIsASnapshot(Boolean isASnapshot) {
        this.isASnapshot = isASnapshot;
    }

    public Long getDomid() {
        return domid;
    }

    public void setDomid(Long domid) {
        this.domid = domid;
    }
}
