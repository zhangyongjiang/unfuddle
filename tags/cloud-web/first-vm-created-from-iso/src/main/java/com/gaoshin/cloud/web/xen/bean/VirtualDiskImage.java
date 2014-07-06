package com.gaoshin.cloud.web.xen.bean;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.xensource.xenapi.Types;

@XmlRootElement
public class VirtualDiskImage {
    private Long id;
    private Long hostId;
    private String uuid;
    private String nameLabel;
    private String nameDescription;
    private Map<String, Types.VdiOperations> currentOperations;
    private String storageReopUuid;
    private Long virtualSize;
    private Long physicalUtilisation;
    private Types.VdiType type;
    private Boolean sharable;
    private Boolean readOnly;
    private Map<String, String> otherConfig;
    private Boolean storageLock;
    private String location;
    private Boolean managed;
    private Boolean missing;
    private String parentVdiUuid;
    private Map<String, String> xenstoreData;
    private Map<String, String> smConfig;
    private Boolean isASnapshot;
    private Long snapshotTime;
    private Set<String> tags;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Map<String, Types.VdiOperations> getCurrentOperations() {
        return currentOperations;
    }

    public void setCurrentOperations(Map<String, Types.VdiOperations> currentOperations) {
        this.currentOperations = currentOperations;
    }

    public Long getVirtualSize() {
        return virtualSize;
    }

    public void setVirtualSize(Long virtualSize) {
        this.virtualSize = virtualSize;
    }

    public Long getPhysicalUtilisation() {
        return physicalUtilisation;
    }

    public void setPhysicalUtilisation(Long physicalUtilisation) {
        this.physicalUtilisation = physicalUtilisation;
    }

    public Types.VdiType getType() {
        return type;
    }

    public void setType(Types.VdiType type) {
        this.type = type;
    }

    public Boolean getSharable() {
        return sharable;
    }

    public void setSharable(Boolean sharable) {
        this.sharable = sharable;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Map<String, String> getOtherConfig() {
        return otherConfig;
    }

    public void setOtherConfig(Map<String, String> otherConfig) {
        this.otherConfig = otherConfig;
    }

    public Boolean getStorageLock() {
        return storageLock;
    }

    public void setStorageLock(Boolean storageLock) {
        this.storageLock = storageLock;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getManaged() {
        return managed;
    }

    public void setManaged(Boolean managed) {
        this.managed = managed;
    }

    public Boolean getMissing() {
        return missing;
    }

    public void setMissing(Boolean missing) {
        this.missing = missing;
    }

    public String getParentVdiUuid() {
        return parentVdiUuid;
    }

    public void setParentVdiUuid(String parentVdiUuid) {
        this.parentVdiUuid = parentVdiUuid;
    }

    public Map<String, String> getXenstoreData() {
        return xenstoreData;
    }

    public void setXenstoreData(Map<String, String> xenstoreData) {
        this.xenstoreData = xenstoreData;
    }

    public Map<String, String> getSmConfig() {
        return smConfig;
    }

    public void setSmConfig(Map<String, String> smConfig) {
        this.smConfig = smConfig;
    }

    public Boolean getIsASnapshot() {
        return isASnapshot;
    }

    public void setIsASnapshot(Boolean isASnapshot) {
        this.isASnapshot = isASnapshot;
    }

    public Long getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(Long snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getStorageReopUuid() {
        return storageReopUuid;
    }

    public void setStorageReopUuid(String storageReopUuid) {
        this.storageReopUuid = storageReopUuid;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
