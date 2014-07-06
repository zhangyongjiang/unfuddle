package com.gaoshin.onsalelocal.yipit.api;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import common.util.JacksonUtil;
import common.util.web.Request;

public class YipitRequest extends Request {
	private static final Logger log = Logger.getLogger(YipitRequest.class);
	
	private String key = "9QyHV3DyMgcmZzm5";
	private String format = "json";
	private Integer limit;
	private Integer offset;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public <T> T call(Class<T> cls) throws Exception {
		String url = toUrl();
		InputStream stream = new URL(url).openStream();
		String resp = IOUtils.toString(stream);
		T value;
		try {
			value = JacksonUtil.json2Object(resp, cls);
		} catch (Exception e) {
			throw new RuntimeException(url + "\n" + resp, e);
		}
		stream.close();
		return value;
	}
}
