package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobDependencyList {
    private List<JobDependency> list = new ArrayList<JobDependency>();

    public List<JobDependency> getList() {
        return list;
    }

    public void setList(List<JobDependency> list) {
        this.list = list;
    }
}
