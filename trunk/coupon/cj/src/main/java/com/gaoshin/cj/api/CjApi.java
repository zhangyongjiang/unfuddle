package com.gaoshin.cj.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.gaoshin.Api;

public class CjApi extends Api{
    private static final String token = "008edb2fc516166be40c3b9feeb3d0fd064c65ee316e1d2d012ebb5ab24aa774040f85d838d2b627f1b6ba862faaf5bbf21f053587e87daf97ce655a6dd09238a1/53a12140018432b9182837a268062a79eb73a893ab60cd1dea1ba826c3e96fd2a49261d256f422fa4aad77542f5a1e6b0dbe83115390269bf45fc515afa0ff59";
    private static final String websiteId = "6194229";
    
    public static void searchLink() {
        
    }
    
    public static String getString(String url) throws IOException {
        HttpResponse response = execute(url);
        InputStream stream = response.getEntity().getContent();
        StringWriter sw = new StringWriter();
        byte[] buff = new byte[8192];
        while(true) {
            int len = stream.read(buff);
            if(len < 0) break;
            sw.write(new String(buff, 0, len));
        }
        stream.close();
        return sw.toString();
    }
    
    public static LinkSearchResponse searchLinks(LinkSearchRequest req) throws Exception {
        String url =  "https://link-search.api.cj.com/v2/link-search?" + req.toString();
        HttpResponse response = execute(url);
        InputStream stream = response.getEntity().getContent();
        JAXBContext ctx = JAXBContext.newInstance(LinkSearchResponse.class);
        Unmarshaller unm = ctx.createUnmarshaller();
        LinkSearchResponse resp = (LinkSearchResponse) unm.unmarshal(stream);
        stream.close();
        return resp;
    }
    
    public static void getCategories() throws IOException {
        String url = "https://support-services.api.cj.com/v2/categories?";
        HttpResponse response = execute(url);
        InputStream stream = response.getEntity().getContent();
        String string = IOUtils.toString(stream);
        System.out.println(string);
    }
    
    public static AdvertiserLookupResponse getAdvertisers(List<String> ids) throws IOException, JAXBException {
        String url = "https://advertiser-lookup.api.cj.com/v3/advertiser-lookup?advertiser-ids=" + ids.toString().replaceAll("[ \\[\\]]+", "");
        HttpResponse response = execute(url);
        InputStream stream = response.getEntity().getContent();
        JAXBContext ctx = JAXBContext.newInstance(AdvertiserLookupResponse.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        AdvertiserLookupResponse resp = (AdvertiserLookupResponse) unmarshaller.unmarshal(stream);
        stream.close();
        return resp;
    }
    
    public static void main(String[] args) throws Exception {
        List<String> test = new ArrayList<String>();
        test.add("1104648");
        test.add("1813911");
        test.add("1834639");
        getAdvertisers(test);
    }
    
    public static void searchLinksTest() throws Exception {
        LinkSearchRequest req = new LinkSearchRequest();
        req.setCategory("Handbags");
        LinkSearchResponse response = searchLinks(req);
        JAXBContext ctx = JAXBContext.newInstance(LinkSearchResponse.class);
        Marshaller m = ctx.createMarshaller();
        m.setProperty("jaxb.formatted.output", true);
        m.marshal(response, System.out);
    }
}
