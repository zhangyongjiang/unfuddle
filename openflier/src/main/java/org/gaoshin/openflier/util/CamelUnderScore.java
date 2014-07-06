package org.gaoshin.openflier.util;

public class CamelUnderScore {
    public static String toCamelCase(String underscored) {
        StringBuilder sb = new StringBuilder();
        String[] items = underscored.split("_");
        for(int i=0; i<items.length; i++) {
            if(i==0) {
                sb.append(items[i]);
            }
            else {
                sb.append(items[i].substring(0, 1).toUpperCase()).append(items[i].substring(1));
            }
        }
        return sb.toString();
    }
    
    public static String underscore(String camel) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<camel.length(); i++) {
            char c = camel.charAt(i);
            if(Character.isUpperCase(c)) {
                if(i > 0) {
                    sb.append("_");
                }
                sb.append(Character.toLowerCase(c));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String camelToLabel(String camel) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<camel.length(); i++) {
            char c = camel.charAt(i);
            if(Character.isUpperCase(c)) {
                if(i > 0) {
                    sb.append(" ");
                }
                sb.append(c);
            }
            else {
            	if(i==0)
            		sb.append(Character.toUpperCase(c));
            	else
            		sb.append(c);
            }
        }
        return sb.toString();
    }
}
