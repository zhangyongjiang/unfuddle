package com.gaoshin.cloud.web.job.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.gaoshin.cloud.web.job.bean.WorkStatus;

@Entity
@Table
public class TaskExecutionEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(length = 64)
	private String name;

	@Column(length = 1024, nullable=false)
	private String handler;

	@Column(length=2048)
	private String args;

	@Column(length = 64)
	@Enumerated(EnumType.STRING)
	private WorkStatus status = WorkStatus.Pending;

	@Column
	private Long expectedDuration;

	@Column
	private Long startTime = System.currentTimeMillis();

	@Column
	private long duration;

	@Column
	private int numOfRetries;

	@Column(nullable=false)
	private int execOrder;

	@Column
	private Integer nextTaskExecOrder;

	@Column
	private int actualNumOfRetries = 0;

	@Column
	private int retryInterval = 0;

    @Column(nullable=false)
    private Long taskId;

    @Column(nullable=false)
    private Long jobExecutionId;

	@Column
	private boolean isErrorHandlingTask = false;

	@Lob
	private String logMsg;

	@Column
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

	public void setExpectedDuration(Long expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	public Long getExpectedDuration() {
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
		this.setActualNumOfRetries(actualNumOfTries);
	}

	public int getActualNumOfTries() {
		return getActualNumOfRetries();
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public int getActualNumOfRetries() {
        return actualNumOfRetries;
    }

    public void setActualNumOfRetries(int actualNumOfRetries) {
        this.actualNumOfRetries = actualNumOfRetries;
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }
}
