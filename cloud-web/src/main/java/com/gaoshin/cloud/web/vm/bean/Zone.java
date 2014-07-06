package com.gaoshin.cloud.web.vm.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Zone {
	private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
