package common.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.DOMParser;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class CrawlerBase {
	private static final Logger logger = Logger.getLogger(CrawlerBase.class);
	
    protected DefaultHttpClient httpClient;
    
    public CrawlerBase() {
        this(1);
    }
    
    public CrawlerBase(int maxConnections) {
        httpClient = new DefaultHttpClient();
    }
    
    public static String getContent(String url) throws IOException {
        InputStream stream = new URL(url).openStream();
        String content = IOUtils.toString(stream);
        return content;
    }

    public String getContentFromUrl(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        InputStream inputStream = response.getEntity().getContent();
        String content = IOUtils.toString(inputStream);
        inputStream.close();
        return content;
    }

    public org.dom4j.Document getDocumentFromUrl(String url) throws Exception {
        try {
	        HttpGet get = new HttpGet(url);
	        HttpResponse response = httpClient.execute(get);
	        InputStream inputStream = response.getEntity().getContent();
	        org.dom4j.Document document = getDocument(inputStream);
	        inputStream.close();
	        return document;
        } catch (Exception e) {
        	logger.error("cannot retrieve info from " + url);
        	throw e;
        }
    }

    public static org.dom4j.Document getDocument(String url) throws Exception {
        DOMParser parser = new DOMParser();
        parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.parse(url);
        Document document = parser.getDocument();
        DOMReader reader = new DOMReader();
        org.dom4j.Document doc = reader.read(document);
        return doc;
    }
    
    public static org.dom4j.Document getDocument(InputStream is) throws Exception {
        DOMParser parser = new DOMParser();
        parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.parse(new InputSource(is));
        Document document = parser.getDocument();
        DOMReader reader = new DOMReader();
        org.dom4j.Document doc = reader.read(document);
        return doc;
    }
    
    public static List<Element> selectElements(org.dom4j.Node doc, String tag, String attrName, String attrValue) {
        return doc.selectNodes(".//" + tag.toUpperCase() + "[@" + attrName + "='" + attrValue + "']");
    }
    
    public static List<Element> selectElements(org.dom4j.Node doc, String tag) {
        return doc.selectNodes(".//" + tag.toUpperCase());
    }
    
    public static Element selectElement(org.dom4j.Node doc, String tag, String attrName, String attrValue) {
        return (Element) doc.selectSingleNode(".//" + tag.toUpperCase() + "[@" + attrName + "='" + attrValue + "']");
    }
    
    public static Element selectElement(org.dom4j.Node doc, String tag) {
        return (Element) doc.selectSingleNode(".//" + tag.toUpperCase());
    }
    
    public static Link getLink(Node node) {
        return getLink((Element) node);
    }
    
    public static String getImg(Element ell) {
        return ell.attributeValue("src");
    }
    
    public static String getImg(Node node) {
        return getImg((Element)node);
    }
    
    public static Link getLink(Element ele) {
        Link link = new Link();
        link.title = ele.getStringValue().trim();
        link.href = ele.attributeValue("href");
        return link;
    }
    
    public static String formatPhone(String phone) {
        if(phone == null || phone.trim().length() == 0)
            return null;
        phone = phone.replaceAll("[\\+\\.\\(\\) \\-]+", "");
        if(phone.length() == 0 || "NULL".equalsIgnoreCase(phone)) {
            return null;
        }
        
        phone = phone.replaceAll("[a|A|b|B|c|C]", "2");
        phone = phone.replaceAll("[d|e|f|D|E|F]", "3");
        phone = phone.replaceAll("[g|h|i|G|H|I]", "4");
        phone = phone.replaceAll("[j|k|l|J|K|L]", "5");
        phone = phone.replaceAll("[m|n|o|M|N|O]", "6");
        phone = phone.replaceAll("[p|q|r|s|P|Q|R|S]", "7");
        phone = phone.replaceAll("[t|u|v|T|U|V]", "8");
        phone = phone.replaceAll("[w|x|y|z|W|X|Y|Z]", "9");
                
        if(phone.startsWith("1") && phone.length() == 11) {
            return phone;
        }
        if(phone.length() == 10) {
            return "1" + phone;
        }
        
        if(!phone.startsWith("1")) {
            phone = "1" + phone;
        }
        return phone.length() > 11 ? phone.substring(0,11) : phone;
    }

    public static String[] getStateAndZip(String stateZip) {
        stateZip = stateZip.toUpperCase().replaceAll("[^A-Z0-9]+", "");
        String[] sz = new String[2];
        sz[0] = stateZip.substring(0,2);
        sz[1] = stateZip.substring(2);
        return sz;
    }
    
    public void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }
    
    public static Map<String, String> getCityStateZip(String line) {
        line = line.trim();
        
        Map<String, String> data = new HashMap<String, String>();
        int pos = line.lastIndexOf(" ");
        String zip = line.substring(pos+1).trim();
        data.put("zipcode", zip);
        
        String storeState = line.substring(pos-2, pos);
        data.put("state", storeState);
        
        String storeCity = line.substring(0, pos - 4).trim();
        data.put("city", storeCity);
        
        return data;
    }
    
    public org.dom4j.Document post(String url, String contentType, String accept, String data) throws Exception {
        try {
	        HttpPost post = new HttpPost(url);
	        if(contentType != null)
	        	post.setHeader("Content-Type", contentType);
	        if(accept != null)
	        	post.setHeader("Accept", accept);
	        post.setEntity(new StringEntity(data));
	        HttpResponse response = httpClient.execute(post);
	        InputStream inputStream = response.getEntity().getContent();
	        org.dom4j.Document document = getDocument(inputStream);
	        inputStream.close();
	        return document;
        } catch (Exception e) {
        	logger.error("cannot retrieve info from " + url + "\n" + contentType + "\naccept: " + accept + "\n" + data);
        	throw e;
        }
    }
}
