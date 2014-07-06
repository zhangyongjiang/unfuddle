
package com.gaoshin.cloud.web.vm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gaoshin.cloud.web.vm.bean.AllocationState;

@Entity
@Table
public class PodEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private Long dataCenterId;

    @Column
    private String name = null;

    @Column
    private String gateway;

    @Column
    private String description;
    
    @Column
    @Enumerated(value=EnumType.STRING)
    private AllocationState allocationState;

    @Column
    private Boolean externalDhcp;
    
    @Column
    private Long removedDate;

    @Column
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(Long removedDate) {
        this.removedDate = removedDate;
    }

}
