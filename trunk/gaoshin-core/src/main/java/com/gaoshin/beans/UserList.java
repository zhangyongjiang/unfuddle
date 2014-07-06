package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserList {
    private List<User> list = new ArrayList<User>();

    public void setList(List<User> list) {
        this.list = list;
    }

    public List<User> getList() {
        return list;
    }
}
