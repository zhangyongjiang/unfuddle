package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY) 
@XmlType(propOrder="")
public class JobExecutionDetailsList {
    private Job job;
	private List<JobExecutionDetails> items = new ArrayList<JobExecutionDetails>();

    public List<JobExecutionDetails> getItems() {
        return items;
    }

    public void setItems(List<JobExecutionDetails> items) {
        this.items = items;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
