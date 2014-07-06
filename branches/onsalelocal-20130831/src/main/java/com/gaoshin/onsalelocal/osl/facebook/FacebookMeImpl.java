package com.gaoshin.onsalelocal.osl.facebook;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import common.util.JacksonUtil;


@Component("facebookMe")
public class FacebookMeImpl implements FacebookMe {
    private static final Logger logger = Logger.getLogger(FacebookMeImpl.class);
    
    private static final String graphUrl = "https://graph.facebook.com/";
    private static final String APP_ID = "149766795141881";
    
    public Map<String, Object> me(String token) {
        String data = fql("select uid, name, username, email, first_name, last_name, middle_name, hometown_location, current_location, pic_small, pic_big, pic_square, pic from user where uid=me()", token, APP_ID);
        try {
	        Map object = JacksonUtil.json2Object(data, JacksonUtil.getTypeRef());
	        List list = (List) object.get("data");
	        return (Map<String, Object>) list.get(0);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
    }
    
    private String fql(String fql, String token, String appId) {
        String json = null;
        InputStream stream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192*2);
        String path = graphUrl+"fql?appId=" + appId + "&format=xml&access_token=" + token;
        
        try {
            path += "&q=" + URLEncoder.encode(fql, "UTF-8");
            URL url = new URL(path);
            stream = url.openStream();
            
            byte[] buff = new byte[8192];
            while(true) {
                int len = stream.read(buff);
                if(len < 0) break;
                baos.write(buff, 0, len);
            }
            json = new String(baos.toByteArray());
        }
        catch (Exception e) {
            logger.error("FacebookException", e);
            throw new RuntimeException(e);
        }
        finally{
            logger.info(path + "\n" + json);
            if(stream != null){ try{ stream.close(); }catch(Exception e){} }
            if(baos != null){ try{ baos.close(); }catch(Exception e){} }
        }
        return json;
    }
    
    public static void main(String[] args) {
        System.out.println(new FacebookMeImpl().me("BAACINkyfovkBAMuAPG8ZApv0sg7sHZBVhFTAZCnYHRdQA5IZARJS7YzGyzf9KKBCG8mN4VFmb2xe7hKMSrA0oHnPJqNehH3wYv9ZAZCEZBl7ZBIyo1iawLbO5E9uDiTAOO0ZD"));
    }
}
