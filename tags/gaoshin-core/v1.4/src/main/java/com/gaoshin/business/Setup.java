package com.gaoshin.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Setup {
    @Autowired
    public void setObjectService(ObjectService objectService) {
        objectService.init();
    }

    @Autowired
    public void setLocationService(LocationService locationService) {
        locationService.init();
    }
}