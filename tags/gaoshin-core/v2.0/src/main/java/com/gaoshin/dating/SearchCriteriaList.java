package com.gaoshin.dating;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchCriteriaList {
    private List<SearchCriteria> list = new ArrayList<SearchCriteria>();

    public void setList(List<SearchCriteria> list) {
        this.list = list;
    }

    public List<SearchCriteria> getList() {
        return list;
    }
}
