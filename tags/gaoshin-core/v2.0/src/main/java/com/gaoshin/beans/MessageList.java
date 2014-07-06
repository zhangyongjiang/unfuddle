package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageList {
    private List<Message> list = new ArrayList<Message>();

    public void setList(List<Message> list) {
        this.list = list;
    }

    public List<Message> getList() {
        return list;
    }
}
