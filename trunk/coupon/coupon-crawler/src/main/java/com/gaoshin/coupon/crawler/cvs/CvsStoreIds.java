package com.gaoshin.coupon.crawler.cvs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;

import com.gaoshin.coupon.crawler.CrawlerBase;
import com.gaoshin.coupon.crawler.Store;

public class CvsStoreIds extends CrawlerBase {
    private static Map<String, Store> good = new HashMap<String, Store>();
    private static List<String> candidates = new ArrayList<String>();
    private static Set<String> candidatesSet = new HashSet<String>();
    private static Set<String> processed = new HashSet<String>();
    
    public static int getStoresFromPage(String url) throws Exception {
        Document doc = getDocument(url);
        List<Element> elements = selectElements(doc, "TABLE", "class", "locstrinfoframe");
        int howmany = 0;
        for(Element ele : elements) {
            howmany++;
            try {
                String addr = ele.selectSingleNode(".//SPAN[@id='locstradd1']").getStringValue().trim();
                if(addr.endsWith(","))
                    addr = addr.substring(0, addr.length()-1);
                String cityStateZip = ele.selectSingleNode(".//SPAN[@id='locstrcitystatezip']").getStringValue();
                String phone = ele.selectSingleNode(".//SPAN[@id='locstrphone']").getStringValue().substring(5);
                String mapLink = getLink(ele.selectSingleNode(".//SPAN[@id='locstrmap']/A")).href;
                int pos = mapLink.lastIndexOf("=");
                String storeid = mapLink.substring(pos+1);
                
                Store store = new Store();
                store.setAddress(addr);
                store.setCity(cityStateZip);
                store.setPhone(formatPhone(phone));
                store.setSecondId(storeid);
                
                if(!good.containsKey(storeid)) {
                    System.out.println(storeid + "\t" + store.getPhone() + "\t" + addr);
                    good.put(storeid, store);
                }
                if(!candidatesSet.contains(storeid) && !processed.contains(storeid)) {
                    candidates.add(storeid);
                    candidatesSet.add(storeid);
                }
            }
            catch (Exception e) {
                System.out.println("ERROR\t" + url);
            }
        }
        return howmany;
    }
    
    public static void getStores(String storeId) throws Exception {
        for(int start=1;;start+=10) {
            String url = "http://weeklyad.cvs.com/cvs/default.aspx?action=browsestorelocation&storeid=" + storeId + "&StartRow=" + start;
            int howmany = getStoresFromPage(url);
            if(howmany ==0 )
                break;
        }
    }
    
    public static void main(String[] args) throws Exception {
        candidates.add("2597017");
        candidatesSet.add("2597017");
        while(!candidates.isEmpty()) {
            String storeid = candidates.remove(0);
            getStores(storeid);
            candidatesSet.remove(storeid);
            processed.add(storeid);
        }
    }

}
