package common.web;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessExceptionResponse {
	private ServiceError errorCode;
	private String data;
	
	public BusinessExceptionResponse() {
	}

	public BusinessExceptionResponse(BusinessException e) {
		errorCode = e.getErrorCode();
		data = e.getData();
	}

	public ServiceError getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ServiceError errorCode) {
		this.errorCode = errorCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
