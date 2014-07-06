package common.geo;


public class PostalAddressParser {
    
    public static PostalAddress parseCityStateZip(String location) {
    	String[] split = location.split("[, ]+");
    	try {
    		int zip = Integer.parseInt(split[split.length-1]);
    		String state = split[split.length - 2];
    		if(state.length() != 2)
        		throw new RuntimeException("invalid location: " + location);
    		StringBuilder sb = new StringBuilder();
    		for(int i=0; i<split.length-2; i++) {
    			if(i>0)
    				sb.append(" ");
    			sb.append(split[i]);
    		}
    		PostalAddress addr = new PostalAddress();
    		addr.setCity(sb.toString());
    		addr.setState(state);
    		addr.setZipcode(String.valueOf(zip));
    		return addr;
    	}
    	catch(Exception e) {
    		throw new RuntimeException("invalid location: " + location, e);
    	}
    }
    
    public static PostalAddress parse(String addr, String city, String state) {
        if(state == null || state.trim().length() == 0)
            state = "UNKNOWN";
        state = state.toUpperCase();
        String tmp = UnitedStatesNameAbbreviation.long2Short.get(state);
        if(tmp != null)
            state = tmp;
        
        if(city == null || city.trim().length() == 0)
            city = "UNKNOWN";
        city = city.toUpperCase();
        
        PostalAddress pa = new PostalAddress();
        pa.setCity(city);
        pa.setState(state);
        pa.setOriginal(addr);
        if(addr == null || addr.trim().length() == 0)
            return pa;
        addr = addr.trim().toUpperCase();
        addr = addr.replaceAll("[^A-Z0-9 ]+", "");
        addr = addr.replaceAll(" +", " ");
        
        String[] items = addr.split(" ");
        for(int i=0; i<items.length; i++) {
            String abbr = PostalAbbreviation.long2Short.get(items[i]);
            if(abbr != null)
                items[i] = abbr;
        }
        
        int streetStart = 0;
        int streetEnd = items.length;
        int secondary = items.length;
        if(items.length > 0) {
            if(SecondaryAddressAbbr.short2Long.containsKey(items[items.length - 1])) {
                secondary = items.length - 1;
                streetEnd = secondary;
            }
        }
        if(secondary == items.length && items.length > 1) {
            if(SecondaryAddressAbbr.short2Long.containsKey(items[items.length - 2])) {
                secondary = items.length - 2;
                streetEnd = secondary;
                pa.setSecNum(items[secondary + 1]);
            }
        }
        if(secondary == items.length && items.length > 0 && items[items.length - 1].length() > 0) {
            char c = items[items.length - 1].charAt(0);
            if(c >= '0' && c <= '9') {
                secondary = items.length - 1;
                streetEnd = secondary;
                pa.setSecNum(items[items.length - 1]);
            }
        }
        if(secondary > -1 && secondary < items.length) {
            pa.setSecondary(items[secondary]);
        }
        
        int direction = -1;
        if(items.length > 0) {
            if(PostalStreetDirectionAbbreviation.short2Long.containsKey(items[0])) {
                direction = 0;
                streetStart = direction + 1;
                pa.setDirection(items[direction]);
            }
        }
        if(direction == -1 && items.length > 1) {
            if(PostalStreetDirectionAbbreviation.short2Long.containsKey(items[1])) {
                direction = 1;
                streetStart = direction + 1;
                pa.setDirection(items[direction]);
                pa.setNumber(items[direction-1]);
            }
        }
        if(direction == -1) {
            if(items.length > 0 && items[0].length() > 0) {
                char c = items[0].charAt(0);
                if(c >= '0' && c <= '9') {
                    pa.setNumber(items[0]);
                    streetStart = 1;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for(int i=streetStart; i>=0 && i<streetEnd && i<items.length; i++) {
            if(i != (streetEnd - 1)) {
                sb.append(items[i]).append(" ");
            }
            else {
                String nonumber = items[i].replaceAll("[0-9]", "");
                if(SecondaryAddressAbbr.short2Long.containsKey(nonumber)) {
                    String secNum = items[i].substring(nonumber.length());
                    secNum = secNum.replaceAll("ST", "");
                    secNum = secNum.replaceAll("ND", "");
                    secNum = secNum.replaceAll("RD", "");
                    secNum = secNum.replaceAll("TH", "");
                    pa.setSecNum(secNum);
                    pa.setSecondary(nonumber);
                }
                else {
                    sb.append(items[i]).append(" ");
                }
            }
        }
        if(sb.length() > 0) {
            pa.setStreet(sb.substring(0, sb.length() - 1));
        }
        
        if(pa.getNumber() != null) {
            String number = Numbers.word2Number.get(pa.getNumber());
            if(number != null)
                pa.setNumber(number);
        }
        
        if(pa.getSecondary() != null) {
            if(!SecondaryAddressAbbr.short2Long.containsKey(pa.getSecondary()))
                pa.setSecondary(null);
        }
        if(pa.getSecondary() != null) {
            if(pa.getStreet() != null) {
                int pos = pa.getStreet().lastIndexOf(" ");
                if(pos != -1) {
                    String number = pa.getStreet().substring(pos + 1);
                    char c = number.charAt(0);
                    if(c>='0' && c<='9') {
                        pa.setStreet(pa.getStreet().substring(0,  pos));
                        pa.setSecNum(number.replaceAll("[^0-9]+", ""));
                    }
                }
            }
        }
        
        if(pa.getSecNum() != null) {
            try {
                pa.setSecNum(String.valueOf(Integer.parseInt(pa.getSecNum())));
            }
            catch (Exception e) {
            }
        }
        
        return pa;
    }
    
}

