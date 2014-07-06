package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobDependency {
	private Long id;
	private Long jobId;
	private Long upstreamJobId;
	private long timeDiff;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTimeDiff(long timeDiff) {
		this.timeDiff = timeDiff;
	}

	public long getTimeDiff() {
		return timeDiff;
	}

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getUpstreamJobId() {
        return upstreamJobId;
    }

    public void setUpstreamJobId(Long upstreamJobId) {
        this.upstreamJobId = upstreamJobId;
    }

}
