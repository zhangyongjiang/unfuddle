package com.gaoshin.mail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.gaoshin.entity.GenericEntity;

@Entity
public class Attachment extends GenericEntity {
    @Column(length = 64)
    private String title;

    @Column(length = 64)
    private String contentType;

    @ManyToOne
    @JoinColumn
    private MailRecord mailRecord;

    @Lob
    private byte[] content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setMailRecord(MailRecord mailRecord) {
        this.mailRecord = mailRecord;
    }

    public MailRecord getMailRecord() {
        return mailRecord;
    }
}
