package com.gaoshin.cj.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "cj-api")
public class AdvertiserLookupResponse {
    private Advertisers advertisers;

    @XmlElement(name="advertisers")
    public Advertisers getAdvertisers() {
        return advertisers;
    }

    public void setAdvertisers(Advertisers advertisers) {
        this.advertisers = advertisers;
    }
}
