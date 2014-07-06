package com.gaoshin.stock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class YahooQuote {
    public static String getQuote(String sym) throws Exception {
        URL url = new URL("http://finance.yahoo.com/q?s=" + sym);
        InputStream stream = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        while(true) {
            String line = br.readLine();
            if(line == null) {
                break;
            }
            if(line.indexOf("yfi_quote_summary_rt_top")!=-1) {
                sb.append(line);
                while(true) {
                    line = br.readLine();
                    if(line == null) {
                        break;
                    }
                    sb.append(line);
                    if(line.indexOf("</div>")!=-1) {
                        break;
                    }
                }
                break;
            }
        }
        stream.close();
        String ret = android.text.Html.fromHtml(sb.toString()).toString().trim();
        ret = ret.replaceAll("Trade Time:", " / ");
        ret = ret.replaceAll("Last Trade:", "Last: ");
        ret = ret.replaceAll("\n\n", "\n");
        
        sb = new StringBuilder();
        for(byte y : ret.getBytes("UTF-8")) {
            if(y >= 9 && y < 0x7f) {
                sb.append((char)y);
            }
        }
        ret = sb.toString();
        return ret;
    }
    
    public static class YQuote {
        public String symbol;
        public String company;
        
        public YQuote(String sym) {
            this.symbol = sym;
        }
        
        public void parse(String s) {
            String[] items = s.split("[\n\r]+");
        }
    }
}
