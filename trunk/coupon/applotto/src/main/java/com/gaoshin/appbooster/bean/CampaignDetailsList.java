package com.gaoshin.appbooster.bean;

import java.util.ArrayList;
import java.util.List;

public class CampaignDetailsList {
    private List<CampaignDetails> items = new ArrayList<CampaignDetails>();

    public List<CampaignDetails> getItems() {
        return items;
    }

    public void setItems(List<CampaignDetails> items) {
        this.items = items;
    }
}
