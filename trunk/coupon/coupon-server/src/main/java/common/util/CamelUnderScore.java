package common.util;

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
    
    public static String upperCaseFirst(String spaced) {
        spaced = spaced.toLowerCase();
        StringBuilder sb = new StringBuilder();
        String[] items = spaced.split(" ");
        for(int i=0; i<items.length; i++) {
            if(items[i].length() == 0 )
                sb.append(" ");
            else
                sb.append(items[i].substring(0, 1).toUpperCase()).append(items[i].substring(1));
            sb.append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    public static void main(String[] args) {
        System.out.println(upperCaseFirst("ABD DEF GH"));
    }
}
