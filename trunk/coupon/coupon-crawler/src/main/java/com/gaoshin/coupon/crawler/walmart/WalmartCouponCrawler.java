package com.gaoshin.coupon.crawler.walmart;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.gaoshin.coupon.crawler.WebCoupon;
import com.gaoshin.coupon.crawler.CouponCategory;
import com.gaoshin.coupon.crawler.Crawler;
import com.gaoshin.coupon.crawler.CrawlerBase;

public class WalmartCouponCrawler extends CrawlerBase implements Crawler {
    public static List<CouponCategory> listCategories() throws Exception {
        List<CouponCategory> list = new ArrayList<CouponCategory>();
        String pageUrl = "http://localad.walmart.com/Walmart/BrowseByListing/ByAllListings";
        Document doc = getDocument(pageUrl);
        List<org.dom4j.Node> nodes = doc.selectNodes("//DIV[@id='CatListLR']/FORM/A");
        for(org.dom4j.Node node : nodes) {
            String txt = node.getText().trim();
            int pos0 = txt.lastIndexOf("(");
            int pos1 = txt.lastIndexOf(")");
            CouponCategory cat = new CouponCategory();
            try {
                cat.setHowmany(Integer.parseInt(txt.substring(pos0+1, pos1)));
            }
            catch (NumberFormatException e) {
            }
            cat.setName(txt.substring(0, pos0).trim());
            String href = ((Element)node).attribute("href").getText();
            cat.setUrl("http://localad.walmart.com" + href);
            pos0 = href.indexOf("CategoryID=");
            if(pos0 == -1)
                continue;
            pos1 = href.indexOf("&", pos0);
            if(pos1 == -1) {
                String id = href.substring(pos0 + 11);
                cat.setId(id);
            }
            else {
                String id = href.substring(pos0) + 11;
                cat.setId(id);
            }
            list.add(cat);
        }
        return list;
    }
    
    public static String getStoreAddress(String storeId) throws Exception {
        String url = "http://localad.walmart.com/Walmart/BrowseByListing/ByAllListings?storeid=" + storeId;
        Document doc = getDocument(url);
        Node node = doc.selectSingleNode("//A[@class='linkToStoreB']");
        return node.getStringValue();
    }
    
    public static List<CouponCategory> listStoreCoupons(String storeId) throws Exception {
        List<CouponCategory> cats = listCategories();
        for (CouponCategory cat : cats) {
            List coupons = listStoreCoupons(storeId, cat.getId());
            cat.setCoupons(coupons);
        }
        return cats;
    }
    
    public static List<WebCoupon> listStoreCoupons(String storeId, String catId) throws Exception {
        List<WebCoupon> coupons = new ArrayList<WebCoupon>();
        for(int page = 0; ; page++) {
            String url = "http://localad.walmart.com/Walmart/BrowseByListing/ByCategory?categoryid=" + catId + "&storeid=" + storeId + "&pagenumber=" + (page+1) + "&listingsort=23";
            Document doc = getDocument(url);
            List<Element> titleNodes = doc.selectNodes("//DIV[@class='listTile']");
            if(titleNodes.size() == 0)
                break;
            
            for(int i=0; i<titleNodes.size(); i++) {
                try {
                    Element element = (Element)titleNodes.get(i).selectSingleNode(".//IMG");
                    String img = element.attribute("src").getStringValue();
                    String id = ((Element)titleNodes.get(i).selectSingleNode(".//DIV[@class='listingAddRem']")
                            .selectSingleNode("A")).attributeValue("id");
                    String title = titleNodes.get(i).selectSingleNode(".//DIV[@class='listingTitle']").getStringValue().replaceAll("[\n\r ]+", " ");
                    String price = titleNodes.get(i).selectSingleNode(".//DIV[@class='listingPrice']").getStringValue().replaceAll("[\n\r ]+", " ");
                    String validDate = titleNodes.get(i).selectSingleNode(".//DIV[@class='listingValidDates']").getStringValue().replaceAll("[\n\r ]+", " ");
                    WebCoupon wc = new WebCoupon();
                    wc.setId(id);
                    wc.setTitle(title);
                    wc.setListPrice(price);
                    wc.setImageUrl(img);
                    wc.setValidDate(validDate);
                    try {
                        String origPrice = titleNodes.get(i).selectSingleNode(".//DIV[@class='fl listingOrigPrice']").getStringValue().replaceAll("[\n\r ]+", " ");
                        wc.setOriginalPrice(origPrice);
                    }
                    catch (Exception e) {
                    }
                    coupons.add(wc);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return coupons;
    }
    
    public static void fetchAllStoreAddress() throws Exception {
        InputStream stream = WalmartCouponCrawler.class.getClassLoader().getResourceAsStream("walmart-storeid.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while(true) {
            String line = br.readLine();
            if(line == null)
                break;
            try {
                String addr = getStoreAddress(line).trim().replaceAll("[\n\r ]+", " ");
                System.out.println(line + "\t" + addr);
            }
            catch (Exception e) {
                System.out.println(line + "\tERROR");
            }
        }
        stream.close();
    }

    public static void main(String[] args) throws Exception {
        String storeId = "2493076";
        List<CouponCategory> categories = listStoreCoupons(storeId );
        System.out.println(categories);
        for (CouponCategory cat : categories) {
            System.out.println("\n=====================");
            System.out.println(cat);
            System.out.println(cat.getCoupons());
        }
    }
}
