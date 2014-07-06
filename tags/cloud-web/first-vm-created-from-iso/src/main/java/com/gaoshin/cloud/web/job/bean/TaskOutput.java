package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = "")
public class TaskOutput {
	private Integer execOrderOfNextTask;

	private int sleepSeconds = 0;

	private KeyValueList keyValueList;
	
	private List<Dependent> blockedby = new ArrayList<Dependent>();

	public void setExecOrderOfNextTask(Integer execOrderOfNextTask) {
		this.execOrderOfNextTask = execOrderOfNextTask;
	}

	public Integer getExecOrderOfNextTask() {
		return execOrderOfNextTask;
	}

	public KeyValueList getKeyValueListCreated() {
		if (keyValueList == null) {
			keyValueList = new KeyValueList();
		}
		return keyValueList;
	}

	public void setKeyValueList(KeyValueList keyValueList) {
		this.keyValueList = keyValueList;
	}

	public KeyValueList getKeyValueList() {
		return keyValueList;
	}

	public void setSleepSeconds(int sleepSeconds) {
		this.sleepSeconds = sleepSeconds;
	}

	public int getSleepSeconds() {
		return sleepSeconds;
	}

	public void setBlockedby(List<Dependent> blockedby) {
		this.blockedby = blockedby;
	}

	public List<Dependent> getBlockedby() {
		return blockedby;
	}

	public static class Dependent {
		private long cronJobId;
		private String timepoint;

		public long getCronJobId() {
			return cronJobId;
		}

		public void setCronJobId(long cronJobId) {
			this.cronJobId = cronJobId;
		}

		public String getTimepoint() {
			return timepoint;
		}

		public void setTimepoint(String timepoint) {
			this.timepoint = timepoint;
		}
	}
}
