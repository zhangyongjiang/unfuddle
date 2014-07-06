package common.util.web;

/**
 * Generic Server-side response 
 * 
 * @author rrajamiyer
 *
 */

public class ServiceResponse {
	
	private String message;
	private int code;

	public ServiceResponse() {}

	public ServiceResponse(String message, int code) {
		super();
		this.message = message;
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	


}
