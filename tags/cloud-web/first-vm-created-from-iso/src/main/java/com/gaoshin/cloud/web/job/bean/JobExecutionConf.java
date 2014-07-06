package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobExecutionConf {
    private Long   id;
	private String ckey;
	private String cvalue;
	private Long jobExecutionId;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
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
