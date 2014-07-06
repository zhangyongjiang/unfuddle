package com.gaoshin.groupon.api;

import java.util.ArrayList;
import java.util.List;

public class LocationList {
    private List<Location> redemptionLocation = new ArrayList<Location>();

    public List<Location> getRedemptionLocation() {
        return redemptionLocation;
    }

    public void setRedemptionLocation(List<Location> redemptionLocation) {
        this.redemptionLocation = redemptionLocation;
    }

}
