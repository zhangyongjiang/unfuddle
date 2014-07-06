package com.gaoshin.onsalelocal.search;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class IdTransformer implements DataImportTransformer {
    Random random = new Random();
    
    @Override
    public boolean transform(Map<String, String> row) {
    	if(!row.containsKey("id")) {
    		row.put("id", UUID.randomUUID().toString());
    	}
        return true;
    }
}
