package com.gaoshin.cloud.web.vm.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Pod {
	private Long id;
	private Long dataCenterId;
    private String name;
    private String gateway;
    private String description;
    private AllocationState allocationState;
    private Boolean externalDhcp;
    private Long removedDate;
    private String uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AllocationState getAllocationState() {
        return allocationState;
    }

    public void setAllocationState(AllocationState allocationState) {
        this.allocationState = allocationState;
    }

    public Boolean getExternalDhcp() {
        return externalDhcp;
    }

    public void setExternalDhcp(Boolean externalDhcp) {
        this.externalDhcp = externalDhcp;
    }

    public Long getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(Long removedDate) {
        this.removedDate = removedDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
