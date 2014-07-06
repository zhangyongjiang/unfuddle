package com.gaoshin.points.server.bean;

public class Configuration {
	private String key;
	private String value;

	public Configuration() {
	}
	
	public Configuration(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value.toString();
    }

    public int getIntValue(int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    public long getLongValue(long def) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean getBooleanValue(boolean def) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(value);
    }

    public long getLongValue() {
        return Long.parseLong(value);
    }
}
