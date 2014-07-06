package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostList {
    private List<Post> list = new ArrayList<Post>();

    public void setList(List<Post> list) {
        this.list = list;
    }

    public List<Post> getList() {
        return list;
    }
}
