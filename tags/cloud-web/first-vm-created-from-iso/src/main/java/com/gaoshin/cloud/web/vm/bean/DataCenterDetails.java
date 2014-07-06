package com.gaoshin.cloud.web.vm.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataCenterDetails extends DataCenter {
    private List<Pod> podList = new ArrayList<Pod>();

    public List<Pod> getPodList() {
        return podList;
    }

    public void setPodList(List<Pod> podList) {
        this.podList = podList;
    }
}
