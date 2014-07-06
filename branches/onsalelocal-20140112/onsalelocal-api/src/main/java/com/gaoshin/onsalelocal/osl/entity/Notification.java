package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class Notification extends DbEntity {
	@Column(nullable = true, length = 64)
	private String userId;

	@Column(nullable = true, length = 64)
	private String eventUserId;

	@Column(nullable = true, length = 64)
	private String offerId;

	@Column(nullable = true, length = 64)
	private String storeId;

    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) 
    private NotificationType type;

    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) 
    private PushStatus pushStatus = PushStatus.Unpushed;

	@Column(nullable = true, length = 1023)
	private String alert;

	@Column(nullable = true, length = 1023)
	private String param;

	@Column(nullable = true)
	private int unread = 1;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getEventUserId() {
	    return eventUserId;
    }

	public void setEventUserId(String eventUserId) {
	    this.eventUserId = eventUserId;
    }

	public String getOfferId() {
	    return offerId;
    }

	public void setOfferId(String offerId) {
	    this.offerId = offerId;
    }

	public String getStoreId() {
	    return storeId;
    }

	public void setStoreId(String storeId) {
	    this.storeId = storeId;
    }

	public NotificationType getType() {
	    return type;
    }

	public void setType(NotificationType type) {
	    this.type = type;
    }

	public PushStatus getPushStatus() {
	    return pushStatus;
    }

	public void setPushStatus(PushStatus pushStatus) {
	    this.pushStatus = pushStatus;
    }

	public int getUnread() {
	    return unread;
    }

	public void setUnread(int unread) {
	    this.unread = unread;
    }

}
