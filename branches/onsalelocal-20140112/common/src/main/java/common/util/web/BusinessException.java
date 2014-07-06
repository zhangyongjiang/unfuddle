package common.util.web;

import common.util.Misc;

public class BusinessException extends RuntimeException {
	private ServiceError errorCode;
	private String data;
	
    public BusinessException(ServiceError errorCode) {
        super("Error code: " + errorCode);
        this.errorCode = errorCode;
    }
    
    public BusinessException(ServiceError errorCode, Throwable t) {
        super("Error code: " + errorCode, t);
        this.errorCode = errorCode;
        data = Misc.getStackTrace(t);
    }
    
	public BusinessException(ServiceError errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public ServiceError getErrorCode() {
		return errorCode;
	}
}
