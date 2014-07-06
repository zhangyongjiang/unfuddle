package com.gaoshin.appbooster.bean;

import com.gaoshin.appbooster.entity.Campaign;

public class CampaignDetails extends Campaign {
    private ApplicationDetails applicationDetails;
    private RewardDetailsList rewardDetailsList = new RewardDetailsList();

    public ApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    public void setApplicationDetails(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
    }

    public RewardDetailsList getRewardDetailsList() {
        return rewardDetailsList;
    }

    public void setRewardDetailsList(RewardDetailsList rewardDetailsList) {
        this.rewardDetailsList = rewardDetailsList;
    }
}
