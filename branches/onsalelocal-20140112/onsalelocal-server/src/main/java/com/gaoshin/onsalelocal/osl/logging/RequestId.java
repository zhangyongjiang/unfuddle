package com.gaoshin.onsalelocal.osl.logging;

public class RequestId {
	private String deviceType = "Unknown";
	private String deviceId = "Unknown";
	private String appVersion = "Unknown";
	private String userId = "Unknown";
	private Float latitude = 0f;
	private Float longitude = 0f;
	private long timestamp = System.currentTimeMillis();
	
	public RequestId() {
    }

	public RequestId(String str) {
		if(str == null || str.trim().length() == 0)
			return;
		String[] split = str.split(";");
		if(split.length < 7)
			return;
		deviceType = split[0];
		deviceId = split[1];
		appVersion = split[2];
		userId = split[3];
		
		if(split[4].length()>0)
		latitude = Float.parseFloat(split[4]);
		
		if(split[5].length()>0)
		longitude = Float.parseFloat(split[5]);
		
		timestamp = Long.parseLong(split[6]);
    }

	public String toString() {
		return deviceType + ";" + deviceId + ";" + appVersion + ";" + userId + ";" + latitude + ";" + longitude + ";" + getTimestamp();
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getDeviceType() {
	    return deviceType;
    }

	public void setDeviceType(String deviceType) {
	    this.deviceType = deviceType;
    }

	public long getTimestamp() {
	    return timestamp;
    }

	public void setTimestamp(long timestamp) {
	    this.timestamp = timestamp;
    }
}
