package common.util.web;

public class ServiceError {
	public static ServiceError NoGuest = new ServiceError(499, "NoGuest");
	public static ServiceError NotFound = new ServiceError(498, "NotFound");
	public static ServiceError Duplicated = new ServiceError(497, "Duplicated");
	public static ServiceError InvalidInput = new ServiceError(496, "InvalidInput");
	public static ServiceError PermissionDenied = new ServiceError(495, "PermissionDenied");
	public static ServiceError NoEnoughBalance = new ServiceError(494, "NoEnoughBalance");
	public static ServiceError SystemError = new ServiceError(493, "SystemError");
	
	private int code;
	private String msg;

	public ServiceError(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public ServiceError() {
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public ServiceError appendMsg(Object obj) {
		this.msg = msg + " " + obj;
		return this;
	}

	@Override
	public String toString() {
		return msg + " " + code;
	}
	
	public String name() {
		return msg;
	}
}
