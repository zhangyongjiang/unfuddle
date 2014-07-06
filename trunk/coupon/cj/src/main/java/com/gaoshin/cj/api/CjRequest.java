package com.gaoshin.cj.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CjRequest {
    private String websiteId = "6194229";
    private int pageNumber = 1;
    private int recordsPerPage = 10;

    public String getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }
    
    @Override
    public String toString() {
        return "website-id=" + websiteId + "&page-number=" + pageNumber + "&records-per-page=" + recordsPerPage + "&";
    }
    
    public void appendUrl(StringBuilder sb, String name, String value) {
        try {
            if(value != null && value.trim().length() > 0)
                sb.append(name).append("=").append(URLEncoder.encode(value, "UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
        }
    }
    
    public void appendUrl(StringBuilder sb, String name, Long value, String format) {
        if(value != null && value.longValue() > 0) {
            Date date = new Date(value);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sb.append(name).append("=").append(sdf.format(date));
        }
    }
    
    public void appendUrl(StringBuilder sb, String name, List value) {
        try {
            if(value != null && value.size() > 0) {
                sb.append(name).append("=");
                for(int i=0; i<value.size(); i++) {
                    if(i>0)
                        sb.append(",");
                    sb.append(URLEncoder.encode(value.get(i).toString(), "UTF-8"));
                }
            }
        }
        catch (UnsupportedEncodingException e) {
        }
    }
}
