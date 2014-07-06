package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobExecutionDetails extends JobExecution {
    private Job job;
    private List<TaskExecution> children = new ArrayList<TaskExecution>();
    private RuntimeConfList runtimeConfList = new RuntimeConfList();

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public List<TaskExecution> getChildren() {
        return children;
    }

    public void setChildren(List<TaskExecution> children) {
        this.children = children;
    }

    public RuntimeConfList getRuntimeConfList() {
        return runtimeConfList;
    }

    public void setRuntimeConfList(RuntimeConfList runtimeConfList) {
        this.runtimeConfList = runtimeConfList;
    }

}
