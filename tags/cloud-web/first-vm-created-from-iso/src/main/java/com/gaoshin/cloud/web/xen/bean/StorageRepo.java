package com.gaoshin.cloud.web.xen.bean;

public class StorageRepo {
	private long id;
    private String uuid;
    private String name;
    private String description;
    private long hostId;
    private String type;
    private long physicalSize;
    private long physicalUtilisation;
    private Boolean shared;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getPhysicalSize() {
        return physicalSize;
    }

    public void setPhysicalSize(long physicalSize) {
        this.physicalSize = physicalSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPhysicalUtilisation() {
        return physicalUtilisation;
    }

    public void setPhysicalUtilisation(long physicalUtilisation) {
        this.physicalUtilisation = physicalUtilisation;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getShared() {
        return shared;
    }
}
