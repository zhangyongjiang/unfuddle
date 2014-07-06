package common.web;

public class BusinessException extends RuntimeException {
	private ServiceError errorCode;
	private String data;
	
	public BusinessException(ServiceError errorCode) {
		super("Error code: " + errorCode);
		this.errorCode = errorCode;
	}
	
	public BusinessException(ServiceError errorCode, String errorMessage) {
		super("Error code: " + errorCode + ". " + errorMessage);
		this.errorCode = errorCode;
        this.data = errorMessage;
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
