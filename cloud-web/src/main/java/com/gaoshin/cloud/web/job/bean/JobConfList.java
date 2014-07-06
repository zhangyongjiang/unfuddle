package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = "")
public class JobConfList {
    private List<JobConf> list = new ArrayList<JobConf>();

    public List<JobConf> getList() {
        return list;
    }

    public void setList(List<JobConf> list) {
        this.list = list;
    }
    
    public JobConf search(String name) {
        for(JobConf jc : list) {
            if(jc.getCkey().equals(name)) {
                return jc;
            }
        }
        return null;
    }
    
}
