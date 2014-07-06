package com.gaoshin.cloud.web.job.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY) 
@XmlType(propOrder="")
public class JobDetails extends Job {
    private JobConfList jobConfList = new JobConfList();
    private TaskList taskList = new TaskList();
    private JobDependencyList jobDependencyList = new JobDependencyList();

    public JobConfList getJobConfList() {
        return jobConfList;
    }

    public void setJobConfList(JobConfList jobConfList) {
        this.jobConfList = jobConfList;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
    
    public JobDependencyList getJobDependencyList() {
        return jobDependencyList;
    }

    public void setJobDependencyList(JobDependencyList jobDependencyList) {
        this.jobDependencyList = jobDependencyList;
    }
}
