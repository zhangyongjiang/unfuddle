package com.gaoshin.web;

import java.util.ArrayList;
import java.util.List;

public class WebUrl {
    private String path;
    private List<UrlParameter> params = new ArrayList<UrlParameter>();

    public WebUrl() {
    }
    
    public WebUrl(String url) {
        int pos = url.indexOf("?");
        if(pos == -1) {
            path = url;
            return;
        }
        
        path = url.substring(0, pos);
        if(pos == (url.length() - 1) ) {
            return;
        }
        
        String query = url.substring(pos+1);
        String[] pairs = query.split("&");
        for(String kv : pairs) {
            if(kv.length() == 0) {
                continue;
            }
            UrlParameter param = new UrlParameter(kv);
            params.add(param);
        }
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<UrlParameter> getParams() {
        return params;
    }

    public void setParams(List<UrlParameter> params) {
        this.params = params;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        if(params.size() > 0 ) {
            sb.append("?");
            for(UrlParameter up : params) {
                sb.append(up.toString()).append("&");
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(new WebUrl("http://yahoo.com"));
        System.out.println(new WebUrl("http://yahoo.com?"));
        System.out.println(new WebUrl("http://yahoo.com?a=1&b=2"));
        System.out.println(new WebUrl("http://yahoo.com?a=1&b=2&c=id%3d34"));
        System.out.println(new WebUrl("http://yahoo.com?a=1&b"));
        System.out.println(new WebUrl("http://yahoo.com?a=1&b="));
        System.out.println(new WebUrl("http://yahoo.com?a"));
    }
}
