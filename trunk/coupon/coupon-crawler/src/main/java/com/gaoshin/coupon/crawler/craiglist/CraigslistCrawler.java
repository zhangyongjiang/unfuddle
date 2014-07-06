package com.gaoshin.coupon.crawler.craiglist;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.gaoshin.coupon.crawler.CrawlerBase;
import com.gaoshin.coupon.crawler.Link;

public class CraigslistCrawler extends CrawlerBase {
    class City {
        String state;
        String area;
        String areaLink;
        String subArea;
        String subAreaLink;
        String city;
    }
    
    public void getUsCities() throws Exception {
        String url = "http://www.craigslist.org/about/sites/";
        Document doc = getDocument(url);
        List<Element> states = selectElements(doc, "DIV", "class", "state_delimiter");
        List<Element> stateUls = selectElements(doc, "UL");
        for(int i=0; i<stateUls.size(); i++) {
            Element ele = states.get(i);
            String state = ele.getStringValue();
            if("Alberta".equals(state))
                break;
            List<Element> areas = selectElements(stateUls.get(i), "A");
            for(Element area : areas) {
                Link link = getLink(area);
                Document areaDoc = getDocument(link.href);
                Element sublink = selectElement(areaDoc, "span", "class", "sublinks");
                List<Element> sublinks = selectElements(sublink, "A");
                for(Element subarea : sublinks) {
                    Link arealink = getLink(subarea);
                    System.out.println(state + "\t" + link.title + "\t" + link.href + "\t" + arealink.title + "\t" + arealink.href);
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        new CraigslistCrawler().getUsCities();
    }
}
