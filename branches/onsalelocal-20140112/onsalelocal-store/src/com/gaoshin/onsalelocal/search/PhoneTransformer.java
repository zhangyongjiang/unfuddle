package com.gaoshin.onsalelocal.search;

import java.util.Map;

public class PhoneTransformer implements DataImportTransformer {
    public static final String KEY = "phone";
    
    @Override
    public boolean transform(Map<String, String> row) {
       String phone = row.get(KEY);
       phone = formatPhone(phone);
       row.put(KEY, phone);
       return true;
    }
    
    public static String formatPhone(String phone) {
        if(phone == null || phone.trim().length() == 0)
            return null;
        phone = phone.replaceAll("[\\+\\.\\(\\) \\-]+", "");
        if(phone.length() == 0 || "NULL".equalsIgnoreCase(phone)) {
            return null;
        }
        
        phone = phone.replaceAll("[a|A|b|B|c|C]", "2");
        phone = phone.replaceAll("[d|e|f|D|E|F]", "3");
        phone = phone.replaceAll("[g|h|i|G|H|I]", "4");
        phone = phone.replaceAll("[j|k|l|J|K|L]", "5");
        phone = phone.replaceAll("[m|n|o|M|N|O]", "6");
        phone = phone.replaceAll("[p|q|r|s|P|Q|R|S]", "7");
        phone = phone.replaceAll("[t|u|v|T|U|V]", "8");
        phone = phone.replaceAll("[w|x|y|z|W|X|Y|Z]", "9");
        phone = phone.replace(":", "");
                
        if(phone.startsWith("1") && phone.length() == 11) {
            return phone;
        }
        if(phone.length() == 10) {
            return "1" + phone;
        }
        
        if(!phone.startsWith("1")) {
            phone = "1" + phone;
        }
        return phone.length() > 11 ? phone.substring(0,11) : phone;
    }
}
