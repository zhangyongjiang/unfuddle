package com.gaoshin.cloud.web.xen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class StorageRepoEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

    @Column(length=64)
    private String uuid;
    
    @Column(length=255)
    private String name;
    
    @Column(length=2047)
    private String description;
    
    @Column(nullable = false)
    private Long hostId;

    @Column(length=64)
    private String type;
    
    @Column
    private Long physicalSize;
    
    @Column
    private Long physicalUtilisation;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getPhysicalSize() {
        return physicalSize;
    }

    public void setPhysicalSize(Long physicalSize) {
        this.physicalSize = physicalSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPhysicalUtilisation() {
        return physicalUtilisation;
    }

    public void setPhysicalUtilisation(Long physicalUtilisation) {
        this.physicalUtilisation = physicalUtilisation;
    }

}
