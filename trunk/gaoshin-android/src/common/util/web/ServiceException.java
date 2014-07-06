package common.util.web;

public class ServiceException extends RuntimeException {
	private int errorCode;
	private String data;
	
	public ServiceException(int errorCode) {
		super("Error code: " + errorCode);
		this.errorCode = errorCode;
	}
	
	public ServiceException(int errorCode, String errorMessage) {
		super("Error code: " + errorCode + ". " + errorMessage);
		this.errorCode = errorCode;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
