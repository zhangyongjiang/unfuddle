package com.gaoshin.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.entity.GenericEntity;

@Entity
public class MailRecord extends GenericEntity {
    @Column(length = 64)
    private String state;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar timestamp;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar sendTime;

    @Column(length = 255)
    private String sender;

    @Column(length = 255)
    private String receipts;

    @Column(length = 255)
    private String cc;

    @Column(length = 255)
    private String subject = "";

    @Column(length = 4096)
    private String content = "";

    @Column(length = 128)
    private String aboutType;

    @Column(length = 32)
    private String aboutId;

    @Column
    private int retry = 1;

    @Column
    private boolean isHtml = true;

    @Column
    private boolean approved = true;

    @OneToMany(mappedBy = "mailRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Attachment> attachmentList = new ArrayList<Attachment>();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setState(Enum state) {
        this.state = state.toString();
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public Calendar getSendTime() {
        return sendTime;
    }

    public void setSendTime(Calendar sendTime) {
        this.sendTime = sendTime;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String body) {
        this.content = body;
    }

    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getRetry() {
        return retry;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setReceipts(String receipts) {
        this.receipts = receipts;
    }

    public String getReceipts() {
        return receipts;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setAboutType(String aboutType) {
        this.aboutType = aboutType;
    }

    public String getAboutType() {
        return aboutType;
    }

    public void setAboutId(String aboutId) {
        this.aboutId = aboutId;
    }

    public String getAboutId() {
        return aboutId;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    @Override
    public String toString() {
        return "from: " + sender + ". to: " + receipts + ". subject: " + subject;
    }
}
