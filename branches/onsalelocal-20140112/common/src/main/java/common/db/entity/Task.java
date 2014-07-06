package common.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import common.db.task.TaskStatus;

@Entity
@Table
public class Task extends DbEntity {
	@Column(nullable=false, length=127) private String type;
	@Column(nullable=false, length=1023) private String param;
	@Column(nullable=false, length=255) private String param1;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private TaskStatus status;
	@Column(nullable=true, length=64) private String handler;
	@Column(nullable=false) private int tried;
	@Column(nullable=false) private int succeed;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getParam() {
		return param;
	}
	
	public void setParam(String param) {
		this.param = param;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public int getTried() {
		return tried;
	}

	public void setTried(int tried) {
		this.tried = tried;
	}

	public int getSucceed() {
		return succeed;
	}

	public void setSucceed(int succeed) {
		this.succeed = succeed;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}
}
