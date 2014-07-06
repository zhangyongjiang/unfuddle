package com.gaoshin.cloud.web.vm.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PodDetails extends Pod {
    private DataCenter dataCenter;
    private ClusterList clusterList = new ClusterList();

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public ClusterList getClusterList() {
        return clusterList;
    }

    public void setClusterList(ClusterList clusterList) {
        this.clusterList = clusterList;
    }
}
