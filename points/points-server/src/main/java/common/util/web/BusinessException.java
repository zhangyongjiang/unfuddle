package common.util.web;

public class BusinessException extends RuntimeException {
	private ServiceError serviceError;
	private String data;
	
	public BusinessException(ServiceError serviceError) {
		super("Error code: " + serviceError);
		this.serviceError = serviceError;
	}
	
	public BusinessException(ServiceError serviceError, String errorMessage) {
		super(errorMessage);
		this.serviceError = serviceError;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}
}
