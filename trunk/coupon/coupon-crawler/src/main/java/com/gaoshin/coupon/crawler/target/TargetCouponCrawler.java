package com.gaoshin.coupon.crawler.target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyberneko.html.parsers.DOMParser;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.w3c.dom.Document;

import com.gaoshin.coupon.crawler.WebCoupon;
import com.gaoshin.coupon.crawler.Crawler;
import com.gaoshin.coupon.crawler.CrawlerBase;

public class TargetCouponCrawler extends CrawlerBase implements Crawler {
    public static Map<String, WebCoupon> listCoupon(String url) throws Exception {
        Map<String, WebCoupon> all = new HashMap<String, WebCoupon>();
        
        loop:
        for (int i = 0;; i++) {
            Map<String, WebCoupon> page = listCoupon(url, i);
            if (page.size() == 0)
                break;
            for(WebCoupon tc : page.values()) {
                if(all.containsKey(tc.getId()))
                    break loop;
                all.putAll(page);
            }
        }
        return all;
    }

    private static Map<String, WebCoupon> listCoupon(String url, int page) throws Exception {
        Map<String, WebCoupon> coupons = new HashMap<String, WebCoupon>();
        String pageUrl = url + "?page=" + (page + 1);
        DOMParser parser = new DOMParser();
        parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.parse(pageUrl);
        Document document = parser.getDocument();
        DOMReader reader = new DOMReader();
        org.dom4j.Document doc = reader.read(document);
        List<org.dom4j.Node> nodes = doc.selectNodes("//A[@class='coupTitle']");
        for(org.dom4j.Node node : nodes) {
            WebCoupon tc = new WebCoupon();
            tc.setTitle(node.getText().trim());
            
            Element imgElem = (Element) node.getParent().selectSingleNode("//IMG");
            String imgUrl = imgElem.attribute("src").getText();
            tc.setImageUrl(imgUrl);
            
            String offer = node.getParent().selectSingleNode("DIV[@class='coupCopy']/A").getText();
            tc.setDescription(offer);
            
            String id = node.getParent().attribute("for").getText().substring(2);
            tc.setId(id);
            
            coupons.put(id, tc);
        }

        return coupons;
    }

    public static void main(String[] args) throws Exception {
    }
}
