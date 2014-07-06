package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Task {
	private Long id;
	private String name;
	private Long jobId;
	private String handler;
	private int numOfRetries = 1;
	private int execOrder;
	private long expectedDuration;
	private int retryInterval = 0;
	private boolean errorHandlingTask = false;
	private String args;
	private boolean disabled = false;
    private String description;

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getHandler() {
		return handler;
	}

	public void setNumOfRetries(int numOfRetries) {
		this.numOfRetries = numOfRetries;
	}

	public int getNumOfRetries() {
		return numOfRetries;
	}

	public void setExpectedDuration(long expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	public long getExpectedDuration() {
		return expectedDuration;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setErrorHandlingTask(boolean errorHandlingTask) {
		this.errorHandlingTask = errorHandlingTask;
	}

	public boolean isErrorHandlingTask() {
		return errorHandlingTask;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getArgs() {
		return args;
	}

	public void setExecOrder(int execOrder) {
		this.execOrder = execOrder;
	}

	public int getExecOrder() {
		return execOrder;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long cronJobId) {
        this.jobId = cronJobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
