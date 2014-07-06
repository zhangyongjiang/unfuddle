package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JobDependencyDetailsList {
    private List<JobDependencyDetails> list = new ArrayList<JobDependencyDetails>();

    public List<JobDependencyDetails> getList() {
        return list;
    }

    public void setList(List<JobDependencyDetails> list) {
        this.list = list;
    }
}
