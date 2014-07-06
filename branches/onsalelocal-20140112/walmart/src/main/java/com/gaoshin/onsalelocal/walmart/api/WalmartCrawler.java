package com.gaoshin.onsalelocal.walmart.api;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import common.crawler.CrawlerBase;
import common.crawler.Link;

public class WalmartCrawler extends CrawlerBase {
	public List<WebCoupon> listWeekSpecial(String storeId) throws Exception {
		List<WebCoupon> result = new ArrayList<WebCoupon>();
		String url = "http://mobile.walmart.com/WeeklySpecials?service=WeeklySpecials&storeId=" + storeId;
        Document doc = getDocumentFromUrl(url);
        List<Element> elements = selectElements(doc, "SPAN", "class", "items-list-span");
        for(Element ele : elements) {
        	String eleTxt = ele.getText();
        	eleTxt = eleTxt.replaceAll("\\\"", "\\\\\"");
            Node catNode = ele.getParent().selectSingleNode(".//SPAN[@class='category-name-span']");
            String category = catNode.getText();
            String specialUrl = "http://mobile.walmart.com/WeeklySpecials/GetCategoryItems";
            String json = "{\"jsonItems\":\"" + eleTxt + "\",\"service\":\"WeeklySpecials\",\"category\":\"" + category + "\"}";
            Document special = post(specialUrl, "application/json; charset=UTF-8", null, json);
            List<Element> products = selectElements(special, "A", "class", "black bold");
            for(Element prod : products) {
            	Link link = getLink(prod);
            	String[] info = link.href.split("\\?")[1].split("&");
            	Map<String, String> map = new HashMap<String, String>();
            	for(String s : info ) {
            		String[] split = s.split("=");
                	map.put(split[0], URLDecoder.decode(split[1]));
            	}
            	
                WebCoupon coupon = new WebCoupon();
                result.add(coupon);
            	coupon.setCategory(category);
            	coupon.setTitle(map.get("Title"));
            	coupon.setListPrice(map.get("Price"));
            	coupon.setDescription(map.get("MorePrice"));
            	coupon.setStartDate(map.get("StartDate"));
            	coupon.setEndDate(map.get("EndDate"));
            	coupon.setImageUrl(map.get("ItemImage"));
            	coupon.setUrl(link.href.startsWith("http") ? link.href : "http://m.walmart.com" + link.href);
            }
        }
        return result;
	}

	public static void main(String[] args) throws Exception {
		WalmartCrawler crawler = new WalmartCrawler();
		for(WebCoupon wc : crawler.listWeekSpecial("1108")) {
        	System.out.println(wc.getTitle() + "\n" + wc.getListPrice() + "\n" + wc.getDescription() + "\n" + wc.getImageUrl() + "\n" + wc.getUrl() + "\n");
		}
    }
}
