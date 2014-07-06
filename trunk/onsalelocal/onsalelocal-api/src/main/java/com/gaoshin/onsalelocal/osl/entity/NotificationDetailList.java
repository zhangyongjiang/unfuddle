package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotificationDetailList {
	private int totalMsg;
	private int totalUnread;
	private List<NotificationDetail> items = new ArrayList<NotificationDetail>();

	public List<NotificationDetail> getItems() {
	    return items;
    }

	public void setItems(List<NotificationDetail> items) {
	    this.items = items;
    }

	public int getTotalMsg() {
	    return totalMsg;
    }

	public void setTotalMsg(int totalMsg) {
	    this.totalMsg = totalMsg;
    }

	public int getTotalUnread() {
	    return totalUnread;
    }

	public void setTotalUnread(int totalUnread) {
	    this.totalUnread = totalUnread;
    }

}
