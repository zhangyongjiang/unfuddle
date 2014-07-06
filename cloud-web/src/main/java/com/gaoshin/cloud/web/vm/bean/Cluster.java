package com.gaoshin.cloud.web.vm.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Cluster {
	private Long id;
	
	private Long podId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPodId() {
        return podId;
    }

    public void setPodId(Long podId) {
        this.podId = podId;
    }

}
