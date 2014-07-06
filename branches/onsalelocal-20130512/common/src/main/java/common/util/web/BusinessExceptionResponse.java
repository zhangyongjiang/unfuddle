package common.util.web;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessExceptionResponse {
	private ServiceError errorCode;
	private String data;
	private String errMsg;
	
	public BusinessExceptionResponse() {
	}

	public BusinessExceptionResponse(BusinessException e) {
		errorCode = e.getErrorCode();
		data = e.getData();
		errMsg = e.getMessage() + "\n" + getStackTrace(e);
	}
	
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
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
