package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AwsItemList {
    private List<AwsItem> list = new ArrayList<AwsItem>();

    public void setList(List<AwsItem> list) {
        this.list = list;
    }

    public List<AwsItem> getList() {
        return list;
    }
}
