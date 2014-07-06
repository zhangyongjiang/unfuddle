package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AwsBrowseNodeList {
    private List<AwsBrowseNode> list = new ArrayList<AwsBrowseNode>();

    public void setList(List<AwsBrowseNode> list) {
        this.list = list;
    }

    public List<AwsBrowseNode> getList() {
        return list;
    }
}
