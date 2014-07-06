package com.gaoshin.coupon.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class UserRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return UserContextHolder.getUserState();
    }

}
