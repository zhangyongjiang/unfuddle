package com.gaoshin.stock.plugin;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class WebUtil {
    private static HashMap<String, byte[]> resources = new HashMap<String, byte[]>();
    
    public static byte[] get(String addr) throws Exception {
        byte[] cache = resources.get(addr);
        if(cache != null) {
            return cache;
        }
        
        URL url = new URL(addr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = conn.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[8192];
        while(true) {
            int len = inputStream.read(buff);
            if(len < 0) {
                break;
            }
            baos.write(buff, 0, len);
        }
        inputStream.close();
        
        byte[] byteArray = baos.toByteArray();
        resources.put(addr, byteArray);
        return byteArray;
    }
}
