package com.gaoshin.onsalelocal.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class NameTransformer implements DataImportTransformer {
    public static final String KEY = "name";
    
    @Override
    public boolean transform(Map<String, String> row) {
       String name = row.get(KEY);
       String orginalName = name;
       if(name == null) {
           System.out.println("place has no name. discard. " + row);
           return false;
       }
       
       row.put("formatted_name", formatName(name));
       name = name.replaceAll("[^a-zA-Z0-9 ]+", " ").replaceAll(" s ", " ").replaceAll(" s$", "");
       row.put(KEY, name);
       
       String concatedName = orginalName.replaceAll("[^a-zA-Z0-9 ]+", "");
       String text = row.get("text");
       if(text == null)
           row.put("text", concatedName);
       else
           row.put("text", text + " " + concatedName);
       
       return true;
    }
    
    public static String formatName(String name1) {
        if (name1 == null) return null;
        name1 = name1.toUpperCase();
        name1 = name1.replaceAll("[^A-Z0-9 ]+", "");
        String[] array1 = name1.split("[ ]+");
        Arrays.sort(array1, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        StringBuilder sb = new StringBuilder();
        for (String s : array1) {
            sb.append(s);
        }
        name1 = sb.toString();
        return name1;
    }
    
    public static void main(String[] args) {
        Map<String, String> row = new HashMap<String, String>();
        row.put("name", "macy's");
        new NameTransformer().transform(row);
        System.out.println(row);
    }
}
