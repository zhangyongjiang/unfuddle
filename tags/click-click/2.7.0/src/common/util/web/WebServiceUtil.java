package common.util.web;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class WebServiceUtil {
	public static <T> T post(String fullUrl, DefaultHttpClient httpclient, Class<T> resultType, Object request) throws Exception {
        String jsonStr = null;
        if (request instanceof String)
            jsonStr = (String) request;
        else
            jsonStr = JsonUtil.toJsonString(request);

		HttpPost httppost = new HttpPost(fullUrl);
		httppost.setHeader("Accept", "application/json;charset=utf-8");
		httppost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
		StringEntity se = new StringEntity(jsonStr, "UTF-8");
		httppost.setEntity(se);
		HttpResponse response;
		response = httpclient.execute(httppost);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) { // getAllHeaders().toString();
			String jsonResp = EntityUtils.toString(response.getEntity(),
					"UTF-8");
			if (resultType.equals(String.class)) {
				return (T) jsonResp;
			} else {
				T bean = JsonUtil.toJavaObject(jsonResp, resultType);
				return bean;
			}
		} else {
			String errMsg = null;
			if(response.getEntity()!=null)
				errMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
			throw new ServiceException(statusCode, errMsg);
		}
	}

	public static <T> T get(DefaultHttpClient httpClient, String path, Class<T> resultType) throws ClientProtocolException, ServiceException, IOException {
		return get(httpClient, path, resultType, "application/json");
	}

	public static <T> T get(DefaultHttpClient httpClient, String fullUrl, Class<T> resultType,
			String accept) throws ClientProtocolException, IOException, ServiceException {
        fullUrl = fullUrl.replace(' ', '+');
		HttpGet httpget = new HttpGet(fullUrl);

		httpget.setHeader("Accept", accept);
		httpget.setHeader(HTTP.CONTENT_TYPE, "application/json");
		HttpResponse response;
		response = httpClient.execute(httpget);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			if (resultType.equals(HttpEntity.class)) {
				HttpEntity httpEntity = response.getEntity();				
		        return (T)httpEntity;		        
			}else if (resultType.equals(String.class)) {
				String jsonResp = EntityUtils.toString(response.getEntity(), "UTF-8");
				return (T) jsonResp;
			}else if (resultType.equals(InputStream.class)) {
				InputStream instream = response.getEntity().getContent();				
		        return (T)instream;	        
			}else {			
				String jsonResp = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (jsonResp == null || jsonResp.trim().length() == 0 || "null".equals(jsonResp))
                    return null;
				T bean = JsonUtil.toJavaObject(jsonResp, resultType);
				return bean;
			}
		} else {
			String errMsg = null;
			if(response.getEntity()!=null)
				errMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
			throw new ServiceException(statusCode, errMsg);
		}
	}

    public static void addCookie(DefaultHttpClient httpClient, String name, String value, String domain) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");

        CookieStore cookieStore = httpClient.getCookieStore();
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
            cookieStore.addCookie(cookie);
            httpClient.setCookieStore(cookieStore);
        } else {
            cookieStore.addCookie(cookie);
        }
    }

    public static String getDomain(String baseUrl) {
        String[] split = baseUrl.split("/");
        String domainPlusPort = split[2];
        if (domainPlusPort.indexOf(":") == -1)
            return domainPlusPort;
        else
            return domainPlusPort.split(":")[0];
    }
}
