package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobConf {
    private Long   id;
	private String ckey;
	private String cvalue;
	private boolean password = false;
	private Long jobId;

	public JobConf() {
	}
	
	public JobConf(String key, String value) {
		this.ckey = key;
		this.cvalue = value;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

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
