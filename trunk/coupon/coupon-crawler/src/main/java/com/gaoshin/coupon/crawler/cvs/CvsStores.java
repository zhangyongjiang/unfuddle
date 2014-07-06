package com.gaoshin.coupon.crawler.cvs;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.gaoshin.coupon.crawler.CrawlerBase;
import com.gaoshin.coupon.crawler.Store;

public class CvsStores extends CrawlerBase {
    public static ArrayList<String> listStates() throws Exception {
        ArrayList<String> links = new ArrayList<String>();
        String url = "http://www.cvs.com/stores/cvs-pharmacy-locations";
        Document doc = getDocument(url);
        List<Element> nodes = doc.selectNodes("//A");
        for(Element ele : nodes) {
            String href = ele.attributeValue("href");
            if(href== null || href.indexOf("/stores/cvs-pharmacy-locations")==-1)
                continue;
            links.add("http://www.cvs.com" + href);
        }
        return links;
    }
    
    public static ArrayList<String> listCities() throws Exception {
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> stateLinks = listStates();
        for(String link : stateLinks) {
            Document doc = getDocument(link);
            List<Element> nodes = doc.selectNodes("//A");
            for(Element ele : nodes) {
                String href = ele.attributeValue("href");
                if(href == null || href.indexOf("/stores/cvs-pharmacy-locations")==-1)
                    continue;
                links.add("http://www.cvs.com" + href);
            }
        }
        return links;
    }
    
    public static ArrayList<Store> listStores() throws Exception {
        ArrayList<Store> stores = new ArrayList<Store>();
        ArrayList<String> cities = listCities();
        for(String link : cities) {
            Document doc = getDocument(link);
            List<Element> nodes = doc.selectNodes("//DIV[@id='main']/TABLE//TR");
            boolean first = true;
            for(Element ele : nodes) {
                if(first) {
                    first = false;
                    continue;
                }
                List<Element> info = ele.selectNodes("TD");
                
                try {
                    String storeLink = info.get(0).getStringValue().replaceAll("[\n\r ]+", " ").trim();
                    int pos = storeLink.lastIndexOf("#");
                    String storeid = storeLink.substring(pos+1).trim();
                    String city = storeLink.substring(0,  pos).trim();
                    
                    String addr = info.get(1).getStringValue().replaceAll("[\n\r ]+", " ").trim();
                    pos = addr.indexOf(city + ",");
                    String[] stateZip = addr.substring(pos + city.length() + 1).trim().split(" ");
                    String state = stateZip[0];
                    String zip = stateZip[1];
                    addr = addr.substring(0, pos).trim();
                    
                    String phone = info.get(2).getStringValue().replaceAll("[\n\r ]+", " ").trim();
                    phone = formatPhone(phone);
                    
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
                catch (Exception e) {
                    System.out.println("ERROR\t" + link);
                }
            }
        }
        return stores;
    }
    
    public static void main(String[] args) throws Exception {
        listStores();
    }
}
