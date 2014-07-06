package com.gaoshin.onsalelocal.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import common.geo.PostalAddress;
import common.geo.UnitedStatesNameAbbreviation;

public class MatchingServlet extends HttpServlet {
    private static final String CsvSeparator = ",";
    public static String SolrServer = "http://localhost:8080";
    private static String[] tfns = { "1800", "1888", "1877", "1866", "1855" };
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        
        int minSimilarity = 75;
        try {
            String acc = getParameter(request, "acc");
            if(acc != null)
                minSimilarity = Integer.parseInt(acc);
        }
        catch (Exception e) {
        }
        
        boolean strictAddressMatch = true;
        String addrMatchLevel     = getParameter(request, "sam", "0");
        strictAddressMatch = "1".equals(addrMatchLevel);
        
        int rows = 1;
        try {
            String row = getParameter(request, "row");
            if(row != null)
                rows = Integer.parseInt(row);
        }
        catch (Exception e) {
        }
        
        String ver = getParameter(request, "ver");
        if(ver==null) ver = "v2";
        
        int index = 0;
        while(true) {
            String surfix = (index == 0) ? "" : String.valueOf(index);
            
            String address = getParameter(request, "address" + surfix);
            String city    = getParameter(request, "city" + surfix);
            String state   = getParameter(request, "state" + surfix);
            String name    = getParameter(request, "name" + surfix);
            String phone   = getParameter(request, "phone" + surfix);
            String id      = getParameter(request, "id" + surfix);
            String cat     = getParameter(request, "cat" + surfix);
            if(address == null && city == null && state == null && name == null && phone == null && id == null)
                break;
            index++;
            
            List<IndexedPlace> places = null;
            try {
                places = match(name, phone, address, city, state, cat, minSimilarity, rows, strictAddressMatch);
            }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            
            if(places.size() == 0) {
                String respLog = "0";
                resp.setHeader("MATCHED", respLog);
            }
            else {
                String respLog = places.size() + " " + places.get(0).getSimilarity() + " " + places.get(0).getId();
                resp.setHeader("MATCHED", respLog);
            }
            
            if(out == null)
                out = resp.getWriter();
            
            int numOfRecords = 0;
            for(IndexedPlace ip : places) {
                numOfRecords++;
                if("v1".equals(ver)) {
                    if(numOfRecords > 1)
                        out.write("\n");
                    if(getParameter(request, "debug") != null) {
                        out.write(String.format("%03d", ip.getSimilarity()));
                        out.write(",");  out.write(ip.getId());
                        try { out.write(",");  out.write(ip.getFormatted_name()); } catch (Exception e) {}
                        try { out.write(",");  out.write(ip.getPhone()); } catch (Exception e) {}
                        try { out.write(",");  out.write(ip.getName()); } catch (Exception e) {}
                        try { out.write(",");  out.write(ip.getAddress()); } catch (Exception e) {}
                        try { out.write(",");  out.write(ip.getCity()); } catch (Exception e) {}
                        try { out.write(",");  out.write(ip.getState()); } catch (Exception e) {}
                    }
                    else {
                        out.write(String.format("%03d", ip.getSimilarity()));
                        out.write(",");  out.write(ip.getId());
                    }
                }
                else if("v2".equals(ver)) {
                    if(numOfRecords > 1)
                        out.write("\n");
                    if(id == null) {
                        out.write(ip.getId());
                    }
                    else {
                        out.write(id); out.write("=");  out.write(ip.getId());
                    }
                }
            }
        }
        
        if(index == 0 && "1".equals(request.getParameter("form"))) {
            resp.setContentType("text/html");
            out = resp.getWriter();
            printForm(out);
        }
    }
    
    private void printForm(PrintWriter out) {
        String html = "<html><body>"
            + "<form><input type='hidden' name='ver' value='v2'/>"
            + "<table>"
            + "<tr><td>Address</td><td><input name=\"address\"/></td></tr>"
            + "<tr><td>City</td><td><input name=\"city\"/></td></tr>"
            + "<tr><td>State</td><td><input name=\"state\"/></td></tr>"
            + "<tr><td>Name</td><td><input name=\"name\" id=\"name\"/></td></tr>"
            + "<tr><td>Phone</td><td><input name=\"phone\" id=\"phone\"/></td></tr>"

            + "<tr><td colspan='2'><hr/></td></tr>"

            + "<tr><td>Address</td><td><input name=\"address1\"/></td></tr>"
            + "<tr><td>City</td><td><input name=\"city1\"/></td></tr>"
            + "<tr><td>State</td><td><input name=\"state1\"/></td></tr>"
            + "<tr><td>Name</td><td><input name=\"name1\" id=\"name1\"/></td></tr>"
            + "<tr><td>Phone</td><td><input name=\"phone1\" id=\"phone1\"/></td></tr>"
            
            + "<tr><td></td><td><input type='submit' value=\"Dedupe\"/></td></tr>"
            
            + "</table>"
            + "</form>"
            + "</body></html>";

        out.write(html);
    }
    
    private String getParameter(HttpServletRequest request, String name, String defValue) {
        String value = getParameter(request, name);
        return value == null ? defValue : value;
    }
    
    private String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if(value == null || value.trim().length() == 0 || "NULL".equalsIgnoreCase(value))
            return null;
        return value.trim();
    }
    
    private int getSimilarity(String name1, String name2, String city, String cat, String fphone, String phoneInSearch) {
        name1 = name1.toUpperCase();
        name2 = name2.toUpperCase();
        if(city != null) {
            city = city.toUpperCase();
            for(String s : city.split(" ")) {
                name1 = name1.replaceAll(s, "");
                name2 = name2.replaceAll(s, "");
            }
        }
        if(cat != null) {
            cat = cat.toUpperCase();
            for(String s : cat.split(" ")) {
                name1 = name1.replaceAll(s, "");
                name2 = name2.replaceAll(s, "");
            }
        }
        
        int len = Math.max(name1.length(), name2.length());
        int dis = StringUtils.getLevenshteinDistance(name1, name2);
        int similarity = 100 - (int)(((float)dis / (float)len) * 100);
        
        if(fphone != null && fphone.equals(phoneInSearch) && fphone.length() > 4)
            similarity += 50;
        
        return similarity;
    }
    
    private boolean isTfn(String phone) {
        for(String s : tfns) {
            if(phone.startsWith(s))
                return true;
        }
        return false;
    }
    
    private List<IndexedPlace> match(String name, String phone, String address, String city, String state, String cat, int minSimilarity, int rows, boolean strictAddressMatch) {
        String searchName = getSearchText(name);
        String fname = AddressFormater.formatName(name);
        String fphone = AddressFormater.formatPhone(phone);
        String fcity = city == null ? null : city.toUpperCase();
        state = state == null ? null : state.toUpperCase();
        String fstate = UnitedStatesNameAbbreviation.long2Short.get(state);
        if(fstate == null)
            fstate = state;
        PostalAddress fstreet = AddressFormater.formatAddress(address, fcity, fstate);
        String faddress = fstreet.getSignature();
        
        if(fcity == null || "NULL".equalsIgnoreCase(fcity)) fcity = "UNKNOWN";
        if(fstate == null || "NULL".equalsIgnoreCase(fstate)) fstate = "UNKNOWN";
        
        List<IndexedPlace> all = new ArrayList<IndexedPlace>();
        Map<String, IndexedPlace> map = new HashMap<String, IndexedPlace>();

        try {
            List<IndexedPlace> list = matchByAddress(searchName, fname, faddress, fcity, fstate);
            for(IndexedPlace ip : list) {
                map.put(ip.getId(), ip);
                String fnameInSearch = ip.getFormatted_name();
                int similarity = getSimilarity(fname, fnameInSearch, city, cat, fphone, ip.getPhone());
                
                try {
                    PostalAddress streetInSearch = AddressFormater.formatAddress(ip.getAddress(), ip.getCity(), ip.getState());
                    if(fstreet.getNumber().equals(streetInSearch.getNumber())) {
                        similarity += 40;
                    }
                    else if (strictAddressMatch){
                        similarity -= 50;
                    }
                }
                catch (Exception e) {
                }
                
                ip.setSimilarity(similarity);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(fphone != null && !isTfn(fphone)) {
            try {
                List<IndexedPlace> list = matchByPhone(searchName, fname, fphone, fcity, fstate);
                for(IndexedPlace ip : list) {
                    String fnameInSearch = ip.getFormatted_name();
                    int similarity = getSimilarity(fname, fnameInSearch, city, cat, fphone, ip.getPhone());
                    
                    try {
                        PostalAddress streetInSearch = AddressFormater.formatAddress(ip.getAddress(), ip.getCity(), ip.getState());
                        if(fstreet.getNumber().equals(streetInSearch.getNumber())) {
                            similarity += 10;
                        }
                        else if (strictAddressMatch){
                            similarity -= 50;
                        }
                    }
                    catch (Exception e) {
                    }
                    
                    ip.setSimilarity(similarity);
                    
                    if(map.containsKey(ip.getId()) && map.get(ip.getId()).getSimilarity() > ip.getSimilarity())
                        continue;
                    
                    map.put(ip.getId(), ip);
                    
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(map.size() == 0) {
            try {
                List<IndexedPlace> list = matchByName(fname, fphone, fcity, fstate);
                all.addAll(list);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        all.addAll(map.values());
        
        Collections.sort(all, new Comparator<IndexedPlace>(){
            @Override
            public int compare(IndexedPlace arg0, IndexedPlace arg1) {
                return arg1.getSimilarity() - arg0.getSimilarity();
            }});

        for(int i=all.size()-1; i>=0; i--) {
            if(all.get(i).getSimilarity() < minSimilarity)
                all.remove(i);
        }
        
        if(all.size() > rows)
            return all.subList(0,  rows);
        else
            return all;
    }
    
    private List<IndexedPlace> matchByName(String fname, String formattedPhone, String fcity, String fstate) throws Exception {
        if(fcity == null || "NULL".equalsIgnoreCase(fcity)) fcity = "UNKNOWN";
        if(fstate == null || "NULL".equalsIgnoreCase(fstate)) fstate = "UNKNOWN";
        fname = URLEncoder.encode(fname, "UTF-8");
        String query = "(formatted_name:\"" + fname + "\")";
        List<IndexedPlace> list = getPlacesFromQuery(query);
        for(int i=list.size()-1; i>=0; i--) {
            IndexedPlace ip = list.get(i);
            boolean samePhone = false;
            if(formattedPhone == null && ip.getPhone() == null)
                samePhone = true;
            else if(formattedPhone != null)
                samePhone = formattedPhone.equals(ip.getPhone());
            if(!fcity.equals(ip.getCity()) 
                    || !fstate.equals(ip.getState()) 
                    || !samePhone
            ) {
                list.remove(i);
            }
            else {
                ip.setSimilarity(100);
            }
        }
        return list;
    }

    private List<IndexedPlace> matchByPhone(String searchName, String fname, String formattedPhone, String fcity, String fstate) throws Exception {
        if(fcity != null && fcity.trim().length() > 0) {
            fcity = URLEncoder.encode(fcity, "UTF-8");
            String query = "phone:" + formattedPhone + "+AND+(text:" + searchName + ")" + "+(city:" + fcity + ")";
            return getPlacesFromQuery(query);
        }
        else {
            String query = "phone:" + formattedPhone + "+AND+(text:" + searchName + ")";
            return getPlacesFromQuery(query);
        }
    }
    
    private List<IndexedPlace> matchByAddress(String searchName, String name, String faddress, String fcity, String fstate) throws Exception {
        faddress = URLEncoder.encode(faddress, "UTF-8");
        if(fcity == null || "NULL".equalsIgnoreCase(fcity)) fcity = "UNKNOWN";
        if(fstate == null || "NULL".equalsIgnoreCase(fstate)) fstate = "UNKNOWN";
        String query = "(formatted_street:\"" + faddress + "\")+AND+(text:" + searchName + ")";
        return getPlacesFromQuery(query);
    }
    
    private List<IndexedPlace> getPlacesFromQuery(String query) throws Exception {
        List<IndexedPlace> result = new ArrayList<IndexedPlace>();
        String path = SolrServer + "/solr/merchant/select/?q=" + query + "&version=2.2&start=0&rows=10&indent=on&fl=id,formatted_name,phone,city,state,formatted_street,address,name&wt=csv&csv.header=false&csv.separator=" + CsvSeparator;
        System.out.println("PLACE MATCHING: " + path);

        URL url = new URL(path);
        InputStream stream = url.openStream();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            while(true) {
                String line = br.readLine();
                if(line == null)
                    break;
                String[] fields = line.split(CsvSeparator);
                IndexedPlace ip = new IndexedPlace();
                result.add(ip);
                ip.setId(getSplittedField(fields, 0));
                ip.setFormatted_name(getSplittedField(fields, 1));
                ip.setPhone(getSplittedField(fields, 2));
                ip.setCity(getSplittedField(fields, 3));
                ip.setState(getSplittedField(fields, 4));
                ip.setFormatted_street(getSplittedField(fields, 5));
                ip.setAddress(getSplittedField(fields, 6));
                ip.setName(getSplittedField(fields, 7));
                if(ip.getName() != null && ip.getName().startsWith("\"")) {
                    ip.setName(ip.getName().substring(1));
                }
            }
        }
        finally {
            try {
                stream.close();
            }
            catch (IOException e) {
            }
        }
        return result;
    }
    
    private String getSplittedField(String[] fields, int index) {
        if(fields.length <= index)
            return null;
        if("".equals(fields[index]))
            return null;
        return fields[index];
    }
    
    protected static String getSearchText(String sentence) {
        int pos = sentence.indexOf("'");
        if(pos==-1) {
            String search = sentence.replaceAll("[^a-zA-Z0-9 ]+", "").replaceAll(" ", "+").toLowerCase();
            return search;
        }
        else {
            int pos0 = sentence.lastIndexOf(" ", pos);
            if(pos0 == -1)
                pos0 = 0;
            int pos1 = sentence.indexOf(" ", pos);
            if(pos1 == -1)
                pos1 = sentence.length();
            String s = sentence.substring(pos0, pos1).replaceAll("'", "");
            String search = (s + " " + sentence).replaceAll("[^a-zA-Z0-9 ]+", " ").replaceAll(" ", "+").toLowerCase();
            return search;
        }
    }
    
    public static Map<String, String> shouldMatch(int minSimilarity) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("./excel.txt"));
        byte[] buff = new byte[8192];
        int total = 0;
        int got = 0;
        int lineNum = 3;
        Map<String, String> result = new HashMap<String, String>();
        while(true) {
            String line = br.readLine();
            if(line == null) break;

            lineNum++;

            String[] split = line.split("\t");
            String name = split.length>0 ? split[0] : null;
            String addr = split.length>1 ? split[1] : null;
            String city = split.length>2 ? split[2] : null;
            String state = split.length>3 ? split[3] : null;
            String phone = split.length>4 ? split[4] : null;

            if(name == null || name.trim().length() == 0)
                continue;

            String value = lineNum + "\t" + line;
            total++;
            {
                String url = "http://match.raved.com/solr/match?"
                        + encode("name", name)
                        + encode("address", addr)
                        + encode("city", city)
                        + encode("state", state)
                        + encode("phone", phone)
                        + encode("sam", "0")
                        + encode("ver", "v1")
                        + encode("debug", "1")
                        + encode("acc", String.valueOf(minSimilarity))
                        ;
                InputStream stream = new URL(url).openStream();
                int len = stream.read(buff);
                stream.close();
                if(len>0) {
                    String string = new String(buff, 0, len);
                    result.put(string, value);
                    got++;
                }
                else {
                    result.put("000\t" + lineNum, value);
                }
                stream.close();
            }
        }
        
        Int stat = new Int(50, 90);
        Object[] scores = result.keySet().toArray();
        Arrays.sort(scores);
        for(int i=scores.length-1; i>=0; i--) {
            Object key = scores[i];
            String value = result.get(key);
            System.out.println("EXCEL:" + value + "\n" + key + "\n");
            stat.inc(Integer.parseInt(key.toString().substring(0, 3)));
        }
        
        System.out.println("total # of match requests: " + total);
        stat.dump();
        return result;
    }
    
    private static class Int {
        int cnt = 0;
        Int parent;
        int index;
        
        public Int(int index, int max) {
            this.index = index;
            if(index < max) {
                parent = new Int(index+1, max);
            }
        }
        
        public void inc(int value) {
            if(value>=index)
                cnt++;
            if(parent != null)
                parent.inc(value);
        }
        
        public void dump() {
            System.out.println(cnt + " matches for score " + index);
            if(parent != null)
                parent.dump();
        }
    }
    
    private static String encode(String name, String value) throws UnsupportedEncodingException {
        if(value == null || value.trim().length() == 0)
            return name + "=&";
        else
            return name + "=" + URLEncoder.encode(value, "UTF-8") + "&";
    }
}
