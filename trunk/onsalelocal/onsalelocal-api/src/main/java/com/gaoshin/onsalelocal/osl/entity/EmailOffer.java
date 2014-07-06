package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class EmailOffer extends DbEntity {
	@Column(nullable = true, length = 255)
    private String receivers;

	@Column(nullable = true, length = 255)
    private String sender;
	
	@Column(nullable = true, length = 255)
    private String subject;
    
	@Column(nullable = true, length = 32767)
	private String content;
	
	@Column(nullable = true, length = 16383)
	private String offer;
	
	@Column(nullable = true)
    private Long sendDate;
	
	@Column(nullable = true)
    private Long recvDate;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getSendDate() {
		return sendDate;
	}

	public void setSendDate(Long sendDate) {
		this.sendDate = sendDate;
	}

	public Long getRecvDate() {
		return recvDate;
	}

	public void setRecvDate(Long recvDate) {
		this.recvDate = recvDate;
	}

	public String getOffer() {
	    return offer;
    }

	public void setOffer(String offer) {
	    this.offer = offer;
    }

	public String getSender() {
	    return sender;
    }

	public void setSender(String sender) {
	    this.sender = sender;
    }

	public String getReceivers() {
	    return receivers;
    }

	public void setReceivers(String receivers) {
	    this.receivers = receivers;
    }
}
