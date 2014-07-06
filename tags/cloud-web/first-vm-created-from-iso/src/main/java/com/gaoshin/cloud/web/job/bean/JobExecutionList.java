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
public class JobExecutionList {
	private List<JobExecution> items = new ArrayList<JobExecution>();

    public List<JobExecution> getItems() {
        return items;
    }

    public void setItems(List<JobExecution> items) {
        this.items = items;
    }
}
