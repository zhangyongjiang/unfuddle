package common.geo;

import java.util.Arrays;
import java.util.Comparator;

public class PostalAddress {
    private final static long MODE = Integer.MAX_VALUE;
    
    private String id;
    private String original;
    private String name;
    private String city;
    private String state;
    
    private String formattedName;
    private String number;
    private String direction;
    private String street;
    private String secondary;
    private String secNum;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSecNum() {
        return secNum;
    }

    public void setSecNum(String secNum) {
        this.secNum = secNum;
    }
    
    @Override
    public String toString() {
        return  (number == null ? "" : (number + " "))
                + (direction == null ? "" : (direction + " ")) + street 
                + (secondary == null ? "" : (" " + secondary)) 
                + (secNum == null ? "" : (" " + secNum)) 
                ;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj == null ? false : obj.toString().equals(toString());
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
    
    public String getSignature() {
        return state + "-" + city + "-" + street;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        formattedName = formatName(name);
    }

    public String getFormattedName() {
        return formattedName;
    }

    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }
    
    public static String formatName(String name1) {
        if(name1 == null) return null;
        name1 = name1.replaceAll("[^A-Z0-9 ]+", "");
        String[] array1 = name1.split("[ ]+");
        Arrays.sort(array1, new Comparator<String>(){
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }});
        StringBuilder sb = new StringBuilder();
        for(String s : array1) {
            sb.append(s);
        }
        name1 = sb.toString();
        return name1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
