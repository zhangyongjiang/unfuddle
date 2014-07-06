package com.gaoshin.coupon.android;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class WebClient {
    private DefaultHttpClient httpClient = null;
    private BasicHttpContext localContext;
    private BasicCookieStore cookieStore;
    
	private synchronized DefaultHttpClient getHttpClient() {
	    if(httpClient == null) {
	        httpClient = new DefaultHttpClient();
            cookieStore = new BasicCookieStore();
            localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	    }
	    return httpClient;
	}
	
	public void httpPost(String resourcePath) throws BusinessException {
		httpPost(resourcePath, String.class, null);
	}
	
	public <T> T httpPost(String fullUrl, Class<T> resultType, Object request) throws BusinessException {
	    HttpResponse response = null;
		try {
			StringWriter sw = new StringWriter();
			if(request == null)
				sw.append(" ");
			else if(request instanceof String)
				sw.append(request.toString());
			else
				sw.append(JsonUtil.toJsonString(request));
			
			String jsonStr = sw.toString();
			
			HttpPost httppost = new HttpPost(fullUrl);
			httppost.setHeader("Accept", "application/json;charset=utf-8");
			httppost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			httppost.setHeader("Accept-Encoding", "deflate, gzip");

			StringEntity se = new StringEntity(jsonStr, "UTF-8");
			
			httppost.setEntity(se);
			response = getHttpClient().execute(httppost, localContext);
			
			return processHttpResponse(response, resultType);
		} catch (BusinessException e) {
			throw e;
		} catch (Throwable t) {
	        StringWriter sw = new StringWriter();
	        t.printStackTrace(new PrintWriter(sw));
			throw new BusinessException(ServiceError.Unknown, sw.toString());
		} finally {
		    if(response != null) {
		        try {
                    response.getEntity().consumeContent();
                } catch (Throwable e) {
                }
		    }
		}
	}

	public <T> T httpGet(String path, Class<T> resultType) throws BusinessException {
		return httpGet(path, resultType, "application/json");
	}
	
	public String httpGetHtml(String fullUrl) {
	    return httpGet(fullUrl, String.class, "text/html");
	}
	
	public <T> T httpGet(String fullUrl, Class<T> resultType, String accept) throws BusinessException {
        HttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(fullUrl);
			httpget.setHeader("Accept", accept);
			httpget.setHeader(HTTP.CONTENT_TYPE, "application/json");
			response = getHttpClient().execute(httpget, localContext);
			return processHttpResponse(response, resultType);
		} catch (BusinessException e) {
			throw e;
		} catch (Throwable t) {
			throw new BusinessException(ServiceError.Unknown, getStackTrace(t));
        } finally {
            if(response != null && !resultType.isAssignableFrom(InputStream.class)) {
                try {
                    response.getEntity().consumeContent();
                } catch (Throwable e) {
                }
            }
        }
	}
	
	private <T> T processHttpResponse(HttpResponse response, Class<T> resultType) throws Exception {
		int statusCode = response.getStatusLine().getStatusCode();
		T bean = null;
		if (statusCode == 200) {
			if(resultType.equals(InputStream.class)){
				bean  = (T) response.getEntity().getContent();
			}else{
				String content = EntityUtils.toString(response.getEntity(),"UTF-8");
				if(content != null && content.length()>0){
					if (resultType.equals(String.class)) {
						bean = (T) content;
					} else if(content.equals("null")) {
						bean = resultType.newInstance();
					} else {
                        bean = (T)JsonUtil.toJavaObject(content, resultType);
					}
				}
			}
		} else {
			ServiceError err = ServiceError.getErrorByCode(statusCode);
			String errMsg = null;
			if(response.getEntity()!=null){
				long preunziplen = response.getEntity().getContentLength();
				errMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
			throw new BusinessException((err!=null ? err : ServiceError.Unknown), errMsg);
		}
		return bean;
	}
	
	public WebClient setCookie(String name, String value) {
	    Cookie cookie = new BasicClientCookie(name, value);
	    cookieStore.addCookie(cookie);
		return this;
	}
	
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
    public static String getCurrentThreadStackTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
