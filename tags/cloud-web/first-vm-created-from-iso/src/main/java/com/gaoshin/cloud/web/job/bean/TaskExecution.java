package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TaskExecution {
	private Long id;
	private String name;
	private String handler;
	private String args;
	private WorkStatus status = WorkStatus.Pending;
	private long expectedDuration;
	private Long startTime = System.currentTimeMillis();
	private long duration;
	private int numOfRetries = 1;
	private int execOrder;
	private Integer nextTaskExecOrder;
	private int actualNumOfTries = 0;
	private int retryInterval = 0;
	private Long jobExecutionId;
	private boolean isErrorHandlingTask = false;
	private String logMsg;
	private boolean disabled = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getHandler() {
		return handler;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStatus(WorkStatus status) {
		this.status = status;
	}

	public WorkStatus getStatus() {
		return status;
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

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public void appendLogMsg(String msg) {
		if(logMsg == null) {
			logMsg = "";
		}
		logMsg = logMsg + msg;
		if(logMsg.length() > 100000) {
			logMsg = logMsg.substring(0, 100000);
		}
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setActualNumOfTries(int actualNumOfTries) {
		this.actualNumOfTries = actualNumOfTries;
	}

	public int getActualNumOfTries() {
		return actualNumOfTries;
	}

	public void setErrorHandlingTask(boolean isErrorHandlingTask) {
		this.isErrorHandlingTask = isErrorHandlingTask;
	}

	public boolean isErrorHandlingTask() {
		return isErrorHandlingTask;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getArgs() {
		return args;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setExecOrder(int execOrder) {
		this.execOrder = execOrder;
	}

	public int getExecOrder() {
		return execOrder;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public String getHtmlLogMsg() {
		if(logMsg == null) {
			return null;
		}
		String html = logMsg.trim();
		html.replaceAll("[\n\r]+", "<br/>");
		return html;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setNextTaskExecOrder(Integer nextTaskExecOrder) {
		this.nextTaskExecOrder = nextTaskExecOrder;
	}

	public Integer getNextTaskExecOrder() {
		return nextTaskExecOrder;
	}

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }
}
