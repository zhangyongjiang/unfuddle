package com.gaoshin.coupon.crawler.target;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.gaoshin.coupon.crawler.WebCoupon;
import com.gaoshin.coupon.crawler.CouponCategory;
import com.gaoshin.coupon.crawler.CrawlerBase;
import com.gaoshin.coupon.crawler.Link;
import com.gaoshin.coupon.crawler.WeeklyAd;

public class TargetWeeklyAdCrawler extends CrawlerBase {
    public static WeeklyAd getWeeklyAds(String zipcode) throws Exception {
        WeeklyAd wa = new WeeklyAd();
        
        String url = "http://sites.target.com/site/en/spot/mobile_weekly_ad.jsp?searchType=weeklyad&zip=" + zipcode;
        Document doc = getDocument(url);
        Element element = selectElement(doc, "P", "class", "pricesGoodRange");
        String validDate = element.getStringValue().trim().substring(12);
        wa.setValidDate(validDate);
        
        List<Element> links = doc.selectNodes("//DIV[@class='product-categories']/UL/LI/A");
        for(Element elem : links) {
            Link link = getLink(elem);
            CouponCategory cc = new CouponCategory();
            cc.setName(link.title);
            String itemListLink = "http://sites.target.com/site/en/spot/" + link.href + "&zip=" + zipcode;
            cc.setUrl(itemListLink);
            wa.getCategories().add(cc);
            
            cc.setCoupons(listCoupons(itemListLink));
        }
        
        return wa;
    }
    
    public static List<WebCoupon> listCoupons(String itemListLink) throws Exception {
        List<WebCoupon> coupons = new ArrayList<WebCoupon>();
        Document doc = getDocument(itemListLink);
        List<Element> items = doc.selectNodes("//OL[@class='products']/LI");
        for(Element elem : items) {
            Node node = elem.selectSingleNode(".//IMG");
            String img = getImg(node);
            String price = elem.selectSingleNode(".//P[@class='price']").getStringValue().trim();
            String desc = elem.selectSingleNode(".//DIV[@class='wa_proddesc']").getStringValue().trim();
            WebCoupon c = new WebCoupon();
            c.setImageUrl(img);
            c.setTitle(desc);
            c.setListPrice(price);
            coupons.add(c);
        }
        return coupons;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(getWeeklyAds("94040").getCategories());
    }
}
