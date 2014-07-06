package com.gaoshin.cloud.web.vm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class GaoHostEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
    @Column
    private Long disconnectedOn;

    @Column(nullable=false)
    private String name = null;

    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    private Status status = null;

    @Column(updatable = true, nullable=false)
    @Enumerated(value=EnumType.STRING)
    private Type type;

    @Column(nullable=false)
    private String privateIpAddress;

    @Column(nullable=false)
    private String privateMacAddress;

    @Column(nullable=false)
    private String privateNetmask;

    @Column
    private String publicNetmask;

    @Column
    private String publicIpAddress;

    @Column
    private String publicMacAddress;

    @Column
    private String storageIpAddress;

    @Column
    private Long clusterId;

    @Column
    private String storageNetmask;

    @Column
    private String storageMacAddress;

    @Column
    private String storageIpAddressDeux;

    @Column
    private String storageNetmaskDeux;

    @Column
    private String storageMacAddressDeux;

    @Column(updatable = true, nullable=false)
    @Enumerated(value=EnumType.STRING)
    private HypervisorType hypervisorType;

    @Column
    private Integer proxyPort;

    @Column
    private String resource;

    @Column
    @Enumerated(value=EnumType.STRING)
    private StoragePoolType fsType;

    @Column
    private boolean available = true;

    @Column
    private boolean setup = false;
    
    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    private ResourceState resourceState;

    @Column
    private String hypervisorVersion;
    
    @Column(updatable = true, nullable=false)
    protected long updated;

    @Column(name="uuid")
    private String uuid;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getPrivateMacAddress() {
        return privateMacAddress;
    }

    public void setPrivateMacAddress(String privateMacAddress) {
        this.privateMacAddress = privateMacAddress;
    }

    public String getPrivateNetmask() {
        return privateNetmask;
    }

    public void setPrivateNetmask(String privateNetmask) {
        this.privateNetmask = privateNetmask;
    }

    public String getPublicNetmask() {
        return publicNetmask;
    }

    public void setPublicNetmask(String publicNetmask) {
        this.publicNetmask = publicNetmask;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public void setPublicIpAddress(String publicIpAddress) {
        this.publicIpAddress = publicIpAddress;
    }

    public String getPublicMacAddress() {
        return publicMacAddress;
    }

    public void setPublicMacAddress(String publicMacAddress) {
        this.publicMacAddress = publicMacAddress;
    }

    public String getStorageIpAddress() {
        return storageIpAddress;
    }

    public void setStorageIpAddress(String storageIpAddress) {
        this.storageIpAddress = storageIpAddress;
    }

    public String getStorageNetmask() {
        return storageNetmask;
    }

    public void setStorageNetmask(String storageNetmask) {
        this.storageNetmask = storageNetmask;
    }

    public String getStorageMacAddress() {
        return storageMacAddress;
    }

    public void setStorageMacAddress(String storageMacAddress) {
        this.storageMacAddress = storageMacAddress;
    }

    public String getStorageIpAddressDeux() {
        return storageIpAddressDeux;
    }

    public void setStorageIpAddressDeux(String storageIpAddressDeux) {
        this.storageIpAddressDeux = storageIpAddressDeux;
    }

    public String getStorageNetmaskDeux() {
        return storageNetmaskDeux;
    }

    public void setStorageNetmaskDeux(String storageNetmaskDeux) {
        this.storageNetmaskDeux = storageNetmaskDeux;
    }

    public String getStorageMacAddressDeux() {
        return storageMacAddressDeux;
    }

    public void setStorageMacAddressDeux(String storageMacAddressDeux) {
        this.storageMacAddressDeux = storageMacAddressDeux;
    }

    public HypervisorType getHypervisorType() {
        return hypervisorType;
    }

    public void setHypervisorType(HypervisorType hypervisorType) {
        this.hypervisorType = hypervisorType;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public StoragePoolType getFsType() {
        return fsType;
    }

    public void setFsType(StoragePoolType fsType) {
        this.fsType = fsType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

    public ResourceState getResourceState() {
        return resourceState;
    }

    public void setResourceState(ResourceState resourceState) {
        this.resourceState = resourceState;
    }

    public String getHypervisorVersion() {
        return hypervisorVersion;
    }

    public void setHypervisorVersion(String hypervisorVersion) {
        this.hypervisorVersion = hypervisorVersion;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getDisconnectedOn() {
        return disconnectedOn;
    }

    public void setDisconnectedOn(Long disconnectedOn) {
        this.disconnectedOn = disconnectedOn;
    }

}
