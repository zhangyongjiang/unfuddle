package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TaskExecutionTry {
    private Long id;
    private Long taskExecutionId;
    private WorkStatus status = WorkStatus.Pending;
    private Long startTime;
    private Long finishTime;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(WorkStatus status) {
        this.status = status;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public Long getTaskExecutionId() {
        return taskExecutionId;
    }

    public void setTaskExecutionId(Long taskExecutionId) {
        this.taskExecutionId = taskExecutionId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }
    
}
