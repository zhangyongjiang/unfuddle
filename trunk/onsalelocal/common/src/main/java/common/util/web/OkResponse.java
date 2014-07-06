package common.util.web;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OkResponse {
	private String code = "success";

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
