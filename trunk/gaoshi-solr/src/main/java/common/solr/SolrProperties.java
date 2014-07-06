package common.solr;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

public class SolrProperties extends SolrObject {
	Properties prop = new Properties();
	
	@SolrField(indexed=false)
	private
	List<String> conf = new ArrayList<String>();

	public void setConf(List<String> conf) {
		this.conf = conf;
		for(String str : conf) {
			int pos = str.indexOf('=');
			String key = str.substring(0, pos);
			String value = str.substring(pos+1);
			prop.setProperty(key, value);
		}
	}

	public List<String> getConf() {
		conf = new ArrayList<String>();
		for(Entry<Object, Object> entry : prop.entrySet()) {
			conf.add(entry.getKey() + "=" + entry.getValue());
		}
		return conf;
	}
	
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	public long getLongProperty(String key, long def) {
		String value = prop.getProperty(key);
		return value == null ? def : Long.parseLong(value);
	}
	
	public int getIntProperty(String key, int def) {
		String value = prop.getProperty(key);
		return value == null ? def : Integer.parseInt(value);
	}
	
	public String getProperty(String key, String def) {
		String value = prop.getProperty(key);
		return value == null ? def : value;
	}
	
	public void setProperty(String key, String value) {
		if(value == null || value.length() == 0)
			prop.remove(key);
		else
			prop.setProperty(key, value);
	}
	
	public void setProperty(String key, long value) {
		prop.setProperty(key, value+"");
	}
	
	public void setProperty(String key, int value) {
		prop.setProperty(key, value+"");
	}

}
