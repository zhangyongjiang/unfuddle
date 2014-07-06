package com.gaoshin.coupon.crawler.safeway;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.gaoshin.coupon.crawler.CrawlerBase;
import com.gaoshin.coupon.crawler.Store;

public class SafewayStores extends CrawlerBase {
    public static ArrayList<String> listStates() throws Exception {
        ArrayList<String> links = new ArrayList<String>();
        String url = "http://local.safeway.com/";
        Document doc = getDocument(url);
        List<Element> nodes = doc.selectNodes("//A[@class='data_list']");
        for(Element ele : nodes) {
            String href = ele.attributeValue("href");
            links.add(href);
        }
        return links;
    }
    
    public static ArrayList<String> listCities() throws Exception {
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> stateLinks = listStates();
        for(String link : stateLinks) {
            Document doc = getDocument(link);
            List<Element> nodes = doc.selectNodes("//A[@class='st_item']");
            for(Element ele : nodes) {
                String href = ele.attributeValue("href");
                links.add(href);
            }
        }
        return links;
    }
    
    public static ArrayList<Store> listStores() throws Exception {
        ArrayList<Store> stores = new ArrayList<Store>();
        ArrayList<String> cities = listCities();
        for(String link : cities) {
            Document doc = getDocument(link);
            int pos;
            String city = null;
            String state = null;
            try {
                Element cityNode = (Element) doc.selectSingleNode("//H1[@class='bigheader']");
                String cityState = cityNode.getStringValue().trim().substring("Safeway Grocery Store Directory in ".length());
                pos = cityState.lastIndexOf(",");
                city = cityState.substring(0, pos).trim();
                state = cityState.substring(pos + 1).trim();
            }
            catch (Exception e1) {
                System.out.println("ERROR\t" + link);
            }
            
            List<Element> nodes = doc.selectNodes("//A[@class='data_list']");
            for(Element ele : nodes) {
                try {
                    String fullAddr = ele.getStringValue().replaceAll("[\n\r ]+", " ").trim().substring(8);
                    pos = fullAddr.indexOf(city + ",");
                    String addr = fullAddr.substring(0, pos);
                    pos = fullAddr.lastIndexOf(" ");
                    String zip = fullAddr.substring(pos + 1);
                    String storeLink = ele.attributeValue("href");
                    pos = storeLink.lastIndexOf("-");
                    int pos1 = storeLink.lastIndexOf(".");
                    String storeid = storeLink.substring(pos+1, pos1);
                    String phone = "";
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
