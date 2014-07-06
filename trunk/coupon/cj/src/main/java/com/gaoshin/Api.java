package com.gaoshin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Api {
    private static Map<Class, JAXBContext> xmlCtx = new HashMap<Class, JAXBContext>();
    
    private static synchronized JAXBContext getXmlContext(Class cls) throws JAXBException {
        JAXBContext ctx = xmlCtx.get(cls);
        if(ctx == null) {
            ctx = JAXBContext.newInstance(cls);
            xmlCtx.put(cls, ctx);
        }
        return ctx;
    }
    
    protected static HttpResponse execute(String url) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("authorization", "008edb2fc516166be40c3b9feeb3d0fd064c65ee316e1d2d012ebb5ab24aa774040f85d838d2b627f1b6ba862faaf5bbf21f053587e87daf97ce655a6dd09238a1/53a12140018432b9182837a268062a79eb73a893ab60cd1dea1ba826c3e96fd2a49261d256f422fa4aad77542f5a1e6b0dbe83115390269bf45fc515afa0ff59");
        HttpResponse response = client.execute(get);
        return response;
    }
    
    protected static <T> T execute(String url, Class<T> cls) throws Exception {
        System.out.println(url);
        HttpResponse response = execute(url);
        InputStream stream = response.getEntity().getContent();
        JAXBContext ctx = getXmlContext(cls);
        Unmarshaller unm = ctx.createUnmarshaller();
        T resp = (T) unm.unmarshal(stream);
        stream.close();
        return resp;
    }
    
    protected static void dumpXml(Object obj) throws JAXBException {
        JAXBContext ctx = getXmlContext(obj.getClass());
        Marshaller m = ctx.createMarshaller();
        m.setProperty("jaxb.formatted.output", true);
        m.marshal(obj, System.out);
    }
}
