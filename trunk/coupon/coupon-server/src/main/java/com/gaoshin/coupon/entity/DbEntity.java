package com.gaoshin.coupon.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class DbEntity implements Serializable{  
    @Id @Column(nullable=false, length=64) private String id;
	@Column(nullable=false) private long created;      
	@Column(nullable=false) private long updated;

    public DbEntity() {
        updated = created = System.currentTimeMillis();
    }
    
	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = this.updated = created;
	}

	public long getUpdated() {
		return updated;
	}

	public void setUpdated(long updated) {
		this.updated = updated;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }      
	
} 
