package com.gaoshin.cloud.web.job.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JobDependencyEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable=false)
	private Long jobId;

	@Column(nullable=false)
	private Long upstreamJobId;

	@Column(nullable=false)
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
