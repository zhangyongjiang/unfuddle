package com.gaoshin.cloud.web.job.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table
public class JobConfEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long   id;
    
	@Column(length=32)
	private String ckey;
	
	@Column(length=2047)
	private String cvalue;
	
	@Column
	private boolean password = false;
	
	@Column(nullable = false)
	private Long jobId;

	public JobConfEntity() {
	}
	
	public JobConfEntity(String key, String value) {
		this.ckey = key;
		this.cvalue = value;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setPassword(boolean isPassword) {
		this.password = isPassword;
	}

	public boolean isPassword() {
		return password;
	}

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long cronJobId) {
        this.jobId = cronJobId;
    }

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }
}
