package common.geo.yahoo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ResultSet")
public class Yresponse {
	private String error;
	private String errorMessage;
	private String locale;
	private String quality;
	private String found;
	private Ylocation result;

	@XmlElement(name="Error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name="ErrorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@XmlElement(name="Local")
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@XmlElement(name="Quality")
	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	@XmlElement(name="Found")
	public String getFound() {
		return found;
	}

	public void setFound(String found) {
		this.found = found;
	}

	@XmlElement(name="Result")
	public Ylocation getResult() {
		return result;
	}

	public void setResult(Ylocation result) {
		this.result = result;
	}

}
