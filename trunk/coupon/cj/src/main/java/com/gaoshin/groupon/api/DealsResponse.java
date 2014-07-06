package com.gaoshin.groupon.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class DealsResponse {
    private DealList deals;

    public DealList getDeals() {
        return deals;
    }

    public void setDeals(DealList deals) {
        this.deals = deals;
    }

}
