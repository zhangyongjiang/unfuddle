package com.gaoshin.appbooster.bean;

import com.gaoshin.appbooster.entity.Reward;

public class RewardDetails extends Reward {
    private CampaignDetails campaignDetails;

    public CampaignDetails getCampaignDetails() {
        return campaignDetails;
    }

    public void setCampaignDetails(CampaignDetails campaignDetails) {
        this.campaignDetails = campaignDetails;
    }

}
