package common.android.contentprovider;


public class Configuration {
	private int id;
	private String key;
	private String value;

	public Configuration() {
	}
	
	public Configuration(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
    public String getValue(String defValue) {
    	if(value == null || value.length() == 0)
    		return defValue;
    	else
    		return value;
    }

    public int getInt(int defValue) {
        return Integer.parseInt(getValue(defValue + ""));
    }

    public float getFloat(float defValue) {
        return Float.parseFloat(getValue(defValue + ""));
    }

    public long getLong(long defValue) {
        return Long.parseLong(getValue(defValue + ""));
    }

    public double getDouble(double defValue) {
        return Double.parseDouble(getValue(defValue + ""));
    }

    public boolean getBoolean(boolean defValue) {
        return Boolean.parseBoolean(getValue(defValue + ""));
    }
}
