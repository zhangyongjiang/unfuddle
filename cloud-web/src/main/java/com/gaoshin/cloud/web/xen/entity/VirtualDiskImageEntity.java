package com.gaoshin.cloud.web.xen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xensource.xenapi.Types;

@Entity
@Table
public class VirtualDiskImageEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(length=64)
    private String uuid;

    @Column(nullable = false)
    private Long hostId;

    @Column(length=64)
    private String nameLabel;

    @Column(length=1023)
    private String nameDescription;

    @Column(length=64)
    private String storageRepoUuid;

    @Column
    private Long virtualSize;

    @Column
    private Long physicalUtilisation;
    
    @Column
    @Enumerated(value=EnumType.STRING)
    private Types.VdiType type;

    @Column
    private Boolean sharable;

    @Column
    private Boolean readOnly;

    @Column
    private Boolean storageLock;

    @Column(length=255)
    private String location;

    @Column
    private Boolean managed;

    @Column
    private Boolean missing;

    @Column(length=64)
    private String parentVdiUuid;

    @Column
    private Boolean isASnapshot;

    @Column
    private Long snapshotTime;

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

    public String getStorageRepoUuid() {
        return storageRepoUuid;
    }

    public void setStorageRepoUuid(String storageRepoUuid) {
        this.storageRepoUuid = storageRepoUuid;
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

}
