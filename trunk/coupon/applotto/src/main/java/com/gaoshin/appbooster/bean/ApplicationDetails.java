package com.gaoshin.appbooster.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.appbooster.entity.Application;

@XmlRootElement
public class ApplicationDetails extends Application {
    private UserDetails userDetails;
    private CampaignDetailsList campaignDetailsList = new CampaignDetailsList();

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public CampaignDetailsList getCampaignDetailsList() {
        return campaignDetailsList;
    }

    public void setCampaignDetailsList(CampaignDetailsList campaignDetailsList) {
        this.campaignDetailsList = campaignDetailsList;
    }

}
