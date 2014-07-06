package com.gaoshin.onsalelocal.search;

import java.util.Map;

import common.geo.PostalAddress;
import common.geo.PostalAddressParser;

public class AddressTransformer implements DataImportTransformer {
    
    @Override
    public boolean transform(Map<String, String> row) {
        String address = (String) row.get("address");
        String city = (String) row.get("city");
        String state = (String) row.get("state");
        PostalAddress pa = PostalAddressParser.parse(address, city, state);
        if(pa.getStreet() != null) {
            row.put("formatted_street", pa.getSignature());
        }
        row.put("city", pa.getCity());
        row.put("state", pa.getState());
        return true;
    }
    
}
