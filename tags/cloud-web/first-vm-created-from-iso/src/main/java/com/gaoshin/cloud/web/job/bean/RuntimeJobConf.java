package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class RuntimeJobConf {
    private Long   id;
	private String ckey;
	private String cvalue;
	private boolean password = false;
	private Long jobExecutionId;

	public RuntimeJobConf() {
	}
	
	public RuntimeJobConf(String key, String value) {
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

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }
}
