package com.gaoshin.entity;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "message")
public class MessageEntity extends GenericEntity {
    @JoinColumn(name = "sender", nullable = false)
    private UserEntity sender;
    
    @JoinColumn(name = "receipt", nullable = false)
    private UserEntity receipt;
    
    @Column(name = "content", nullable = false, length = 1023)
    private String content;
    
    @Basic(optional = true)
    @Column
    private Boolean html;
    
    @Basic(optional = true)
    @Column(name="is_read")
    private boolean read;
    
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    
	public MessageEntity() {
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setHtml(Boolean html) {
		this.html = html;
	}

	public Boolean isHtml() {
		return html;
	}

    public void setRead(boolean read) {
        this.read = read;
	}

    public boolean isRead() {
        return read;
	}

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setReceipt(UserEntity receipt) {
        this.receipt = receipt;
    }

    public UserEntity getReceipt() {
        return receipt;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

}
