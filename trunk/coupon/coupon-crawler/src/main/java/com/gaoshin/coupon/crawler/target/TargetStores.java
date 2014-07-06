package com.gaoshin.coupon.crawler.target;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.gaoshin.coupon.crawler.CrawlerBase;
import com.gaoshin.coupon.crawler.Store;

public class TargetStores extends CrawlerBase {
    public static ArrayList<String> listStates() throws Exception {
        ArrayList<String> links = new ArrayList<String>();
        String url = "http://www.target.com/store-locator/state-listing";
        Document doc = getDocument(url);
        List<Element> nodes = doc.selectNodes("//A[@class='statelink']");
        for(Element ele : nodes) {
            links.add("http://www.target.com/store-locator/" + ele.attributeValue("href"));
        }
        return links;
    }

    public static ArrayList<Store> listStores() throws Exception {
        ArrayList<Store> stores = new ArrayList<Store>();
        ArrayList<String> states = listStates();
        for(String link : states) {
            Document doc = getDocument(link);
            List<Element> nodes = doc.selectNodes("//TR[@class='data-row']");
            for(Element ele : nodes) {
                List<Element> info = ele.selectNodes("TD");
                String addr = info.get(2).getStringValue().replaceAll("[\n\r ]+", " ").trim();
                
                String[] cityStateZip = info.get(3).getStringValue().replaceAll("[\n\r ]+", " ").trim().split(",");
                String city = cityStateZip[0];
                String state = cityStateZip[1].substring(0, cityStateZip[1].length()-5);
                String zip = cityStateZip[1].substring(cityStateZip[1].length()-5);
                
                String phone = info.get(4).getStringValue().replaceAll("[\n\r ]+", " ").trim();
                phone = formatPhone(phone);
                
                String storeLink = ((Element)info.get(1).selectSingleNode("A")).attributeValue("href");
                int pos = storeLink.lastIndexOf("=");
                String storeid = storeLink.substring(pos+1);
                
                Store store = new Store();
                store.setId(storeid);
                store.setAddress(addr);
                store.setCity(city);
                store.setState(state);
                store.setZipcode(zip);
                store.setPhone(phone);
                stores.add(store);
                
                System.out.println(storeid + "\t" + phone + "\t" + zip + "\t" + state + "\t" + city + "\t" + addr);
            }
        }
        return stores;
    }
    
    public static void main(String[] args) throws Exception {
        listStores();
    }
}
