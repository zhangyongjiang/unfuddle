package common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class FacebookFqlUtil {
    
    public static String me(String token) {
        return fql("select uid, name, username, email, first_name, last_name, middle_name, hometown_location, current_location, pic_small, pic_big, pic_square, pic from user where uid=me()", token);
    }
    
    public static String listMyFriends(String token) {
        return fql("select uid, name, username, email, first_name, last_name, middle_name, hometown_location, current_location, pic_small, pic_big, pic_square, pic from user WHERE uid in (SELECT uid2 FROM friend WHERE uid1 = me())", token);
    }
    
    public static String listCheckins(String token, String userId){
        return fql("SELECT checkin_id, page_id, author_uid, timestamp, message FROM checkin WHERE author_uid = "+userId, token);
    }
    public static String listPageFans(String token, String userId){
        return fql("SELECT uid, page_id, type, profile_section, created_time FROM page_fan WHERE uid = "+userId, token);
    }
    
    public static List<String> listCheckins(String token, Collection<String> userIds){return listCheckins(token, userIds, null, null);}
    public static List<String> listCheckins(String token, Collection<String> userIds, Long start){return listCheckins(token, userIds, start, null);}
    
    public static List<String> listCheckins(String token, Collection<String> userIds, Long startTime, Long endTime) {
        final String fql0 = "SELECT checkin_id, page_id, author_uid, timestamp, message FROM checkin " +
                "WHERE author_uid in (__IN__) __START_TIME__ __END_TIME__ limit 5000 ";
        int size = 20;
        List<List<String>> ids = split(userIds, size);
        List<String> result = new ArrayList<String>();
        for(List<String> group : ids) {
            String fql = fql0;
            String in = listToString(group);
            fql = fql.replaceAll("__IN__", in);
            
            if(startTime != null && startTime > 0l) {
                fql = fql.replaceAll("__START_TIME__", " and timestamp>=" + (startTime/1000l));
            }
            else {
                fql = fql.replaceAll("__START_TIME__", "");
            }
            
            if(endTime != null && endTime > 0l) {
                fql = fql.replaceAll("__END_TIME__", " and timestamp<" + (endTime/1000l));
            }
            else {
                fql = fql.replaceAll("__END_TIME__", "");
            }
            
            try {
                result.add(fql(fql, token));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<String> listPageFans(String token, Collection<String> userIds){ return listPageFans(token,userIds,null,null);}
    public static List<String> listPageFans(String token, Collection<String> userIds, Long start){ return listPageFans(token,userIds,start,null);}
    
    public static List<String> listPageFans(String token, Collection<String> userIds, Long startTime, Long endTime) {
        String fql0 = "SELECT uid, page_id, type, profile_section, created_time FROM page_fan " +
                "WHERE uid in (__IN__) __START_TIME__ __END_TIME__ limit 5000 ";
        int size = 20;
        List<List<String>> ids = split(userIds, size);
        List<String> result = new ArrayList<String>();
        for(List<String> group : ids) {
            String fql = fql0;
            String in = listToString(group);
            fql = fql.replaceAll("__IN__", in);
            
            if(startTime != null && startTime > 0l) {
                fql = fql.replaceAll("__START_TIME__", " and created_time>=" + (startTime/1000l));
            }
            else {
                fql = fql.replaceAll("__START_TIME__", "");
            }
            
            if(endTime != null && endTime > 0l) {
                fql = fql.replaceAll("__END_TIME__", " and created_time<" + (endTime/1000l));
            }
            else {
                fql = fql.replaceAll("__END_TIME__", "");
            }
            
            try {
                result.add(fql(fql, token));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public static List<String> getPages(String token, List<String> pages) {
        List<List<String>> ids = split(pages, 100);
        List<String> result = new ArrayList<String>();
        for(List<String> group : ids) {
            String in = listToString(group);
            String fql = "SELECT page_id,name,type,categories,phone,location,website, hours, description, pic, pic_small, pic_big, pic_large, pic_square FROM page WHERE page_id in (" + in + ")";
            result.add(fql(fql, token));
        }
        return result;
    }
    
    public static String getPagePosts(String token, String pageId, long time){
        return fql("SELECT post_id, source_id, actor_id, permalink, message, description, created_time, attachment FROM stream WHERE source_id = "+pageId+
                " and actor_id = source_id and created_time>="+(time/1000l)+" limit 300 ", token);
    }
    
    public static List<String> getPagePosts(String token, Collection<String> pageIds){ return getPagePosts(token,pageIds,null,null);}
    public static List<String> getPagePosts(String token, Collection<String> pageIds, Long start){ return getPagePosts(token,pageIds,start,null);}
    
    public static List<String> getPagePosts(String token, Collection<String> pageIds, Long startTime, Long endTime) {
        String fql0 = "SELECT post_id, source_id, actor_id, permalink, message, description, created_time, attachment FROM stream "+
                    "WHERE source_id in (__IN__) and actor_id = source_id __START_TIME__ __END_TIME__ limit 5000 ";
        int size = 20;
        List<List<String>> ids = split(pageIds, size);
        List<String> result = new ArrayList<String>();
        for(List<String> group : ids) {
            String fql = fql0;
            String in = listToString(group);
            fql = fql.replaceAll("__IN__", in);
            
            if(startTime != null && startTime > 0l) {
                fql = fql.replaceAll("__START_TIME__", " and created_time>=" + (startTime));
            }
            else {
                fql = fql.replaceAll("__START_TIME__", "");
            }
            
            if(endTime != null && endTime > 0l) {
                fql = fql.replaceAll("__END_TIME__", " and created_time<" + (endTime));
            }
            else {
                fql = fql.replaceAll("__END_TIME__", "");
            }
            try {
                String temp = fql(fql, token);
                result.add(temp);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    private static List<List<String>> split(Collection<String> bag, int size) {
        List<String> input = new ArrayList<String>(bag);
        List<List<String>> ret = new ArrayList<List<String>>();
        List<String> current = null;
        while(input.size() > 0) {
            if(current == null || current.size()>=size) {
                current = new ArrayList<String>();
                ret.add(current);
            }
            current.add(input.remove(0));
        }
        return ret;
    }
    
    private static String listToString(Collection<String> list) {
        StringBuilder sb = new StringBuilder();
        for(String s : list) {
            sb.append(","+s);
        }
        return sb.substring(1);
    }
    
    public static String fql(String fql, String token){return fql(fql,token,APP_ID);}
    
    public static String fql(String fql, String token, String appId) {
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
            System.out.println(json);
            throw new RuntimeException(e);
        }
        finally{
            if(stream != null){ try{ stream.close(); }catch(Exception e){} }
            if(baos != null){ try{ baos.close(); }catch(Exception e){} }
        }
        return json;
    }
    
    public static String postForm(String url, String token, HashMap<String, String> form) {
        PostMethod method = new PostMethod(graphUrl+url);
        method.addParameter("access_token",token);
        for(String key : form.keySet()){
            method.addParameter(key,form.get(key));
        }
        String ret = null;
        try{
            new HttpClient().executeMethod(method);
            ret = method.getResponseBodyAsString();
        }catch(Exception e){}
        return ret;
    }
    
    public static final String graphUrl = "https://graph.facebook.com/";
    public static final String fbUrl = "https://www.facebook.com/";
    public static final String APP_ID = "149766795141881";
    public static final String APP_ID_DEV = "164482970338131";
    public static final long DAY = 86400000l;
    public static void main(String[] args) throws Exception {
        String token = "BAACVmK0Ew1MBAF88phdZC9Ve0JnXGcud5s3lzWzqfUGOxnPjur6R7YM2mV46b533aFt2ZB8Kl6ZCgGCEJMFZCTXWhQX0ONCxdqry9cQaauVIEJDevVN3d3SiKfkQ4FsZD";
        
        HashMap<String,String> form = new HashMap<String, String>();
        form.put("message", "Brian Raved Willow Street");
        form.put("name", "Raved");
        form.put("caption", "Sunnyvale, CA" );
        form.put("type", "photo");
        //form.put("source", "http://127.0.0.1/200041826684900/picture" );
        //form.put("object_id", "314074168658969");
        form.put("picture", "http://msg.raved.com/message-prod/facebook/img/152042211528833");
        String resp = FacebookFqlUtil.postForm("/me/feed", token, form);
        System.out.println(resp);
    }
}
