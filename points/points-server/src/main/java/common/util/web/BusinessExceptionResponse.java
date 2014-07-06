package common.util.web;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessExceptionResponse {
	private ServiceError errorCode;
	private String data;
	private String errMsg;
	
	public BusinessExceptionResponse() {
	}

	public BusinessExceptionResponse(BusinessException e) {
		errorCode = e.getServiceError();
		data = e.getData();
		errMsg = e.getMessage();
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

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}
}
