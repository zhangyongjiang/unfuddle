package com.gaoshin.onsalelocal.slocal.api;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class ShoplocalCrawler extends CrawlerBase {
    public static Map<String, String> listCategories() throws Exception {
        Map<String, String> cats = new HashMap<String, String>();
        String url = "http://www.shoplocal.com/category.fp";
        Document doc = getDocument(url);
        List<Element> elements = doc.selectNodes("//A");
        String parent = null;
        for(Element ele : elements) {
            Link link = getLink(ele);
            String style = ele.attributeValue("class");
            String title = link.title;
            String href = "http://www.shoplocal.com" + link.href;
            if("boldlink12".equals(style)) {
                parent = title;
                System.out.println(title + "\t\t" + href);
            }
            if("plainlink12".equals(style)) {
                System.out.println(title + "\t" + parent + "\t" + href);
            }
        }
        return cats;
    }
    
    public Document changeLocation(String loc) throws Exception {
        String url = "http://www.shoplocal.com/ChangeShoppingZoneHandler.ashx?redirect=http%3A%2F%2Fwww.shoplocal.com%2Fcategorylist.aspx&newzone=Y&citystatezip=" + URLEncoder.encode(loc, "UTF-8") + "&x=0&y=0";
        return getDocumentFromUrl(url);
    }
    
    public String changeLocation2(String loc) throws Exception {
        String url = "http://www.shoplocal.com/ChangeShoppingZoneHandler.ashx?redirect=http%3A%2F%2Fwww.shoplocal.com%2Fcategorylist.aspx&newzone=Y&citystatezip=" + URLEncoder.encode(loc, "UTF-8") + "&x=0&y=0";
        return getContentFromUrl(url);
    }
    
    public Document changeLocation(String loc, String next) throws Exception {
        String url = "http://www.shoplocal.com/ChangeShoppingZoneHandler.ashx?redirect=" + URLEncoder.encode(next, "UTF-8") + "&newzone=Y&citystatezip=" + URLEncoder.encode(loc, "UTF-8") + "&x=0&y=0";
        return getDocumentFromUrl(url);
    }
    
    public String getLocationUrl(String loc, String next) throws Exception {
        String url = "http://www.shoplocal.com/ChangeShoppingZoneHandler.ashx?redirect=" + URLEncoder.encode(next, "UTF-8") + "&newzone=Y&citystatezip=" + URLEncoder.encode(loc, "UTF-8") + "&x=0&y=0";
        return url;
    }
    
    public int getStoresV2(String city, String state, FileWriter fw) throws Exception {
        int total = 0;
        String path = "http://www.shoplocal.com/stores.fp";
        Document doc = changeLocation(city + "," + state, path);
        List<Element> elements = selectElements(doc, "A");
        for(Element ele : elements) {
            String id = ele.attributeValue("id");
            if(id== null || !id.startsWith("ctl00_ContentPlaceHolder1_allStores_allStoresLinks_ct") || !id.endsWith("_retailerNameLink"))
                continue;
            Link link = getLink(ele);
            String storeName = link.title;
            String storeUrl = "http://www.shoplocal.com" + link.href;
            System.out.println("Store URL: " + storeUrl);
            String content = getContentFromUrl(storeUrl);
            BufferedReader br = new BufferedReader(new StringReader(content));
            int print = 0;
            int info = 0;
            Store store = new Store();
        	String logo = null;
            total++;
            store.setName(storeName);
            while(true) {
                String line = br.readLine();
                if(line == null)
                    break;
            	store.setUrl(storeUrl);
                if(line.indexOf("ctl00_ContentPlaceHolder1_storeHeader_storeLogoImage")!=-1) {
                	int pos = line.indexOf("http://akimages.shoplocal.com");
                	if(pos != -1) {
	                    int pos1 = line.indexOf("\"", pos);
	                    if(pos1 != -1) {
	                    	logo = line.substring(pos, pos1);
	                    	store.setLogo(logo);
	                    }
                	}                    
                }
                if(line.indexOf("SalesFull.aspx?cc_pretailerid") != -1) {
                    String pattern = "cc_storeid=";
                    int pos = line.indexOf(pattern);
                    int pos1 = line.indexOf("&", pos);
                    String storeId = line.substring(pos + pattern.length(), pos1);
                    store.setId(storeId);
                }
                if(print > 0) {
                    line = line.trim();
                    if(!"<br />".equals(line)) {
                        line = line.replaceAll("<br />", "").trim();
                        if(line.length() > 0) {
                            if(info == 0) {
                                store.setAddress(line);
                            }
                            else if(info == 1) {
                                Map<String, String> map = getCityStateZip(line);
                                store.setCity(map.get("city"));
                                store.setState(map.get("state"));
                                store.setZipcode(map.get("zipcode"));
                            }
                            else if(info == 2) {
                                store.setPhone(line);
                            }
                            info++;
                        }
                    }
                    print--;
                    continue;
                }
                if(line.indexOf(storeName) == -1)
                    continue;
                if(line.indexOf(storeName+"</h1>") != -1) {
                    print = 3;
                    info = 0;
                }
                else if (line.indexOf("<a rel=\"nofollow\"") != -1) {
                    System.out.println(store);
                    fw.write(store.toString());
                    fw.write("\n");
                    fw.flush();
                    store = new Store();
                    store.setUrl(storeUrl);
                    store.setLogo(logo);
                    total++;
                    store.setName(storeName);
                    String pattern = "?storeid=";
                    int pos = line.indexOf(pattern);
                    int pos1 = line.indexOf("\"", pos);
                    String storeId = line.substring(pos + pattern.length(), pos1);
                    store.setId(storeId);
                    print = 6;
                    info = 0;
                }
            }
            System.out.println(store);
            fw.write(store.toString());
            fw.write("\n");
            fw.flush();
        }
        
        return total;
    }
    
    public int getStore(String city, String state, String categoryId, FileWriter fw) throws Exception {
        String path = "http://www.shoplocal.com/browsestores.aspx?N=" + categoryId;
        int total = 0;
        for(int page = 0; ;page++) {
            String url = path;
            if(page > 0) {
                url += "&No=" + (page * 20);
            }
            Document doc = changeLocation(city + "," + state, url);
            List<Element> elements = selectElements(doc, "DIV");
            int howmany = 0;
            for(Element ele : elements) {
                String id = ele.attributeValue("id");
                if(id== null || !id.startsWith("ctl00_ContentPlaceHolder1_resultsRepeater_ctl") || !id.endsWith("_thumbnailDiv"))
                    continue;
                Store store = new Store();
                howmany++;
                List<Element> divs = selectElements(ele, "DIV");
                
                Element imgTag = selectElement(divs.get(0), "IMG");
                String img = getImg(imgTag);
                
                Element nameTag = selectElement(divs.get(1), "A");
                Link link = getLink(nameTag);
                String name = link.title;
                String storeUrl = link.href.trim();
                String storeId = storeUrl.substring(storeUrl.lastIndexOf("=")+1);
                
                Element addrDiv = selectElement(divs.get(1), "DIV", "class", "pl22");
                String[] address = addrDiv.getStringValue().split("[\n\r]+");
                int index = 0;
                while(address[index].trim().length() == 0)
                    index++;
                store.setAddress(address[index].trim());
                
                index++;
                while(address[index].trim().length() == 0)
                    index++;
                int pos = address[index].lastIndexOf(" ");
                String zip = address[index].substring(pos+1).trim();
                String storeState = address[index].substring(pos-2, pos);
                String storeCity = address[index].substring(0, pos - 4).trim();
                
                store.setId(storeId);
                store.setName(name);
                store.setUrl(storeUrl);
                store.setCity(storeCity);
                store.setState(storeState);
                store.setZipcode(zip);
                store.setLogo(img);
                
                System.out.println(store.toString());
                fw.write(store.toString());
                fw.write("\n");
                fw.flush();
                total++;
            }
            if(howmany == 0) {
                break;
            }
        }
        return total;
    }
    
    public List<WebCoupon> getCoupons(String url) throws Exception {
        List<WebCoupon> coupons = new ArrayList<WebCoupon>();
        for(int i=0; i<20; i+=20) {
            String page = (i==0) ? url : (url + "?No=" + i);
            List<WebCoupon> couponsInPage = getCouponsInPage(page);
            coupons.addAll(couponsInPage);
            if(couponsInPage.size() == 0) {
                break;
            }
        }
        return coupons;
    }
    
    public List<WebCoupon> getCouponsFromHtml(String html) throws Exception {
        Document doc = getDocument(new ByteArrayInputStream(html.getBytes()));
        return getCouponsInPage(doc);
    }
    
    public List<WebCoupon> getCouponsInPage(String url) throws Exception {
        Document doc = getDocumentFromUrl(url);
        return getCouponsInPage(doc);
    }
    
    public List<WebCoupon> getCouponsInPage(Document doc) throws Exception {
        List<WebCoupon> coupons = new ArrayList<WebCoupon>();
        List<Element> elements = selectElements(doc, "DIV", "class", "detailUnitGeneral");
        for(Element ele : elements) {
            WebCoupon c = new WebCoupon();
            c.setSource("SL");
            try {
                Node node = ele.selectSingleNode(".//DIV[@class='theImage']//IMG");
                if(node != null) {
                    String img = getImg(node);
                    c.setImageUrl(img);
                    c.setUrl("http://www.shoplocal.com" + node.getParent().attributeValue("href"));
                }
                
                Node priceNode = ele.selectSingleNode(".//DIV[@class='floatleft itemPriceNew']");
                List<Element> priceDate = priceNode.selectNodes(".//SPAN");
                for(Element pd : priceDate) {
                    String id = pd.attributeValue("id");
                    if(id.endsWith("_price")) {
                        String price = pd.getStringValue().trim();
                        if(price.startsWith("$")) {
                            try {
                                price = String.valueOf(Float.parseFloat(price.substring(1)));
                            }
                            catch (Exception e) {
                            }
                        }
                        c.setListPrice(price);
                    }
                    else if(id.endsWith("_startDate")) {
                        c.setStartDate(pd.getStringValue().trim());
                    }
                    else if(id.endsWith("_endDate")) {
                        c.setEndDate(pd.getStringValue().trim());
                    }
                }
                
                List<Element> nodes = ele.selectNodes(".//DIV[@class='descrip']/DIV/A");
                Link link = getLink(nodes.get(0));
                String title = link.title;
                c.setTitle(title);
                
                link = getLink(nodes.get(1));
                c.setCompany(link.title);
                
                List<Node> addrNodes = nodes.get(2).selectNodes(".//SPAN");
                c.setAddress(addrNodes.get(0).getStringValue());
                String cityStateZip = addrNodes.get(1).getStringValue();
                int pos = cityStateZip.indexOf(",");
                c.setCity(cityStateZip.substring(0,  pos));
                String[] stateAndZip = getStateAndZip(cityStateZip.substring(pos+1));
                c.setState(stateAndZip[0]);
                c.setZipcode(stateAndZip[1]);
                
                if(addrNodes.size() > 1) {
                	c.setPhone(formatPhone(addrNodes.get(2).getStringValue()));
                }
                
                coupons.add(c);
            }
            catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage() + "\n" + ele.getStringValue());
                e.printStackTrace();
                coupons.add(null);
            }
        }
        return coupons;
    }
    
    public static void main(String[] args) throws Exception {
//        String loc = "NEW YORK,NY";
//        String next = "http://www.shoplocal.com/aarons.fp";
//        String url = "http://www.shoplocal.com/ChangeShoppingZoneHandler.ashx?redirect=" + URLEncoder.encode(next, "UTF-8") + "&newzone=Y&citystatezip=" + URLEncoder.encode(loc, "UTF-8") + "&x=0&y=0";
//        System.out.println(url);
        
        ShoplocalCrawler crawler = new ShoplocalCrawler();
        FileWriter fw = new FileWriter("./target/stores.tsv");
        crawler.getStoresV2("NEW YORK", "NY", fw);
        fw.close();
        
//        crawler.changeLocation("Mountain View, CA");
//        List<WebCoupon> coupons = crawler.getCoupons("http://www.shoplocal.com/baby-food.fp");
//        for(WebCoupon wc : coupons) {
//        	System.out.println(wc.getAddress() + ", " + wc.getPhone());
//        }
    }

    public static WebCoupon getDetails(String url) throws Exception {
        Document doc = getDocument(url);
        String description = selectElement(doc, "P", "class", "pt10").getStringValue();
        WebCoupon c = new WebCoupon();
        c.setDescription(description);
        return c;
    }
    
    public SlocalCategory crawlCategories() throws Exception {
        changeLocation("New York,NY");
        String home = "http://www.shoplocal.com/storehome.aspx";
        SlocalCategory cat = new SlocalCategory();
        FileWriter fw = new FileWriter("./target/cats.tsv");
        try {
            HashMap<String, SlocalCategory> catmap = new HashMap<String, SlocalCategory>();
            listCategories(cat, home, catmap, fw);
        }
        finally {
            fw.close();
        }
        return cat;
    }

    private void listCategories(SlocalCategory cat, String url, HashMap<String, SlocalCategory> catmap, FileWriter fw) throws Exception {
        Document doc = getDocument(url);
        List<Element> elements = selectElements(doc, "A");
        for(Element ele : elements) {
            Link link = getLink(ele);
            String pattern = "/browsestores.aspx?N=";
            if(link.href != null && link.href.startsWith(pattern)) {
                String catId = link.href.substring(pattern.length());
                if(catId.indexOf("%20") == -1 && !cat.equals(link.title)) {
                    SlocalCategory child = new SlocalCategory();
                    child.setParent(cat);
                    child.setId(catId);
                    child.setLink(link.href);
                    child.setName(link.title);
                    cat.getChildren().add(child);
                    fw.write(child.toString());
                    fw.write("\n");
                    System.out.println(child.toString());
                    if(!catmap.containsKey(catId)) {
                        catmap.put(catId, child);
                        listCategories(child, "http://www.shoplocal.com" + link.href, catmap, fw);
                    }
                }
            }
        }
    }

	public List<WebCoupon> getTopDeals(String url) throws Exception {
		List<WebCoupon> result = new ArrayList<WebCoupon>();
        Document doc = getDocumentFromUrl(url);
        List<Element> elements = selectElements(doc, "DIV", "class", "floatleft fontgrayb topDealsThumbsTight");
        for(Element ele : elements) {
        	Element imgdiv = selectElement(ele, "DIV", "class", "floatleft topDealsInfoColumnTight");
        	String img = getImg(selectElement(imgdiv, "IMG"));
        }
	    return result;
    }
}
