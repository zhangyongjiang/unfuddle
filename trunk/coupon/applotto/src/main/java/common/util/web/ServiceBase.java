package common.util.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import common.util.JacksonUtil;

public class ServiceBase {
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private static HttpParams params = null;
	private static ClientConnectionManager cm = null;
	private static CookieStore cookieStore = null;
	static {
        params = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpConnectionParams.setConnectionTimeout(params, 15000);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        cookieStore = new BasicCookieStore();
	}
	
	private static DefaultHttpClient getDefaultHttpClient() {
	    DefaultHttpClient client = new DefaultHttpClient(cm, params);
	    client.setCookieStore(cookieStore);
	    return client;
	}
	
	protected String server = null;
	protected ObjectMapper jsonProcessor = new ObjectMapper();
	private DefaultHttpClient httpClient = null;
	private Map<String, String> cookieMap = new HashMap<String, String>();

	public ServiceBase(String server) {
		this(server, getDefaultHttpClient());
	}

	public ServiceBase(String server, DefaultHttpClient httpClient) {
		this.server = server;
		this.httpClient = httpClient;
		jsonProcessor.configure(Feature.WRITE_NULL_PROPERTIES, false);
		jsonProcessor.configure(
				org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	public String getServer() {
		return server;
	}
	
	public String getServiceUrl(String path) {
		return server + path;
	}

	protected Document getDocument(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
		return factory.newDocumentBuilder().parse(inputStream);
	}

	protected Document getDocument(String content) throws SAXException, IOException, ParserConfigurationException {
		InputSource inputSource = new InputSource(new StringReader(content));
		return factory.newDocumentBuilder().parse(inputSource);
	}
	
	public void httpPost(String resourcePath) throws BusinessException {
		httpPost(resourcePath, String.class, " ");
	}
	
	public <T> T httpPost(String resourcePath, Class<T> resultType, Object request) throws BusinessException {
	    HttpResponse response = null;
		try {
			String fullUrl = getServiceUrl(resourcePath);
			
			StringWriter sw = new StringWriter();
			if(request == null)
				sw.append(" ");
			else if(request instanceof String)
				sw.append(request.toString());
			else
				jsonProcessor.writeValue(sw, request);
			String jsonStr = sw.toString();

			HttpPost httppost = new HttpPost(fullUrl);
			httppost.setHeader("Accept", "application/json;charset=utf-8");
			httppost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			StringEntity se = new StringEntity(jsonStr, "UTF-8");
			httppost.setEntity(se);
			response = getHttpClient().execute(httppost);
			return processHttpResponse(response, resultType);
		} catch (BusinessException e) {
			throw e;
		} catch (Throwable t) {
			throw new BusinessException(ServiceError.Unknown, t.getMessage());
		} finally {
		    if(response != null) {
		        try {
                    response.getEntity().consumeContent();
                } catch (Throwable e) {
                }
		    }
		}
	}

	public <T> T httpGet(String path, Class<T> resultType) throws BusinessException{
		return httpGet(path, resultType, "application/json");
	}

	public HashMap<String, Object> httpGet(String path) throws BusinessException
			{
		return httpGet(path, HashMap.class, "application/json");
	}

	public String getHtml(String path) throws ClientProtocolException, BusinessException, IOException {
		return httpGet(path, String.class, "text/html");
	}

	public <T> T httpGet(String resourcePath, Class<T> resultType,
			String accept) throws BusinessException {
        HttpResponse response = null;
		try {
			String fullUrl = getServiceUrl(resourcePath);
			HttpGet httpget = new HttpGet(fullUrl);
			httpget.setHeader("Accept", accept);
			httpget.setHeader(HTTP.CONTENT_TYPE, "application/json");
			response = getHttpClient().execute(httpget);
			return processHttpResponse(response, resultType);
		} catch (BusinessException e) {
			throw e;
		} catch (Throwable t) {
			throw new BusinessException(ServiceError.Unknown, t.getMessage());
        } finally {
            if(response != null) {
                try {
                    response.getEntity().consumeContent();
                } catch (Throwable e) {
                }
            }
        }
	}
	
	private <T> T processHttpResponse(HttpResponse response, Class<T> resultType) throws JsonParseException, JsonMappingException, IOException, InstantiationException, IllegalAccessException {
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			String jsonResp = EntityUtils.toString(response.getEntity(),
					"UTF-8");
			if (resultType.equals(String.class)) {
				return (T) jsonResp;
			} else if (resultType.equals(HashMap.class)) {
				return (T) jsonProcessor.readValue(new StringReader(jsonResp),
						JacksonUtil.getTypeRef());
			} else {
				if(jsonResp == null || jsonResp.length() == 0) {
						return null;
				}
				if(jsonResp.equals("null")) {
					return resultType.newInstance();
				}
				T bean = null;
				try {
					bean = jsonProcessor.readValue(new StringReader(jsonResp), resultType);
				} catch (Exception e) {
					System.out.println("---------- jsonResp: " + jsonResp);
					e.printStackTrace();
				}
				return bean;
			}
		} else {
			ServiceError err = ServiceError.getErrorByCode(statusCode);
			System.out.println("---------- web service returns error. status code is " + statusCode + ", errorCode is " + err);
			String errMsg = null;
			if(response.getEntity()!=null)
				errMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (err == null) {
				err = ServiceError.Unknown;
			}
			throw new BusinessException(err, errMsg);
		}
	}

	public DefaultHttpClient getHttpClient() {
		return httpClient;
	}
	
	public ServiceBase setCookie(String name, String value) {
	    Cookie cookie = new BasicClientCookie(name, value);
	    cookieStore.addCookie(cookie);
		return this;
	}
	
	public String getCookieString() {
		if(cookieMap.isEmpty())
			return null;
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> entry : cookieMap.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
		}
		return sb.toString();
	}
}
