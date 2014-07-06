package com.gaoshin.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Notification {
    private Long id;
    private User from;
    private User to;
    private Long sentTime;
    private Long receiveTime;
    private MsgType type;
    private MsgType subtype;
    private String msg;
    private String title;
    private String url;
    private boolean read;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setSentTime(Long sentTime) {
        this.sentTime = sentTime;
    }

    public Long getSentTime() {
        return sentTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public MsgType getType() {
        return type;
    }

    public void setSubtype(MsgType subtype) {
        this.subtype = subtype;
    }

    public MsgType getSubtype() {
        return subtype;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getFrom() {
        return from;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public User getTo() {
        return to;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isRead() {
        return read;
    }
}
