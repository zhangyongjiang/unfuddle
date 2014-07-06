package com.gaoshin.appbooster.market;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.appbooster.entity.ApplicationType;
import com.gaoshin.appbooster.service.AppResolver;

@Component
public class Markets {
    private Map<ApplicationType, AppResolver> markets = new HashMap<ApplicationType, AppResolver>();
    
    @Autowired
    public void addAndroidMarket(AndroidMarketAppResolver resolver) {
        markets.put(resolver.getType(), resolver);
    }
}
