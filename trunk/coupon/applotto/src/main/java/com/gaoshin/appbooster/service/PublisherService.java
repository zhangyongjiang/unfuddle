package com.gaoshin.appbooster.service;

import com.gaoshin.appbooster.bean.ApplicationDetailsList;
import com.gaoshin.appbooster.bean.ApplicationList;
import com.gaoshin.appbooster.bean.RewardDetails;
import com.gaoshin.appbooster.bean.RewardDetailsList;
import com.gaoshin.appbooster.bean.UserDetails;
import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.Campaign;
import com.gaoshin.appbooster.entity.Reward;

public interface PublisherService {
    Application addApplication(Application app);

    Reward addReward(String userId, Reward reward);

    ApplicationList listUserApplications(String userId);

    void removeReward(String userId, String rewardId);

    UserDetails getUserDetails(String userId);

    ApplicationDetailsList latestApplications(int size);

    RewardDetailsList latestRewards(int size);

    Application getApplication(String applicationId);

    RewardDetails getRewardDetails(String rewardId);

    Reward updateReward(String userId, Reward reward);

    Campaign addCampaign(String userId, Campaign campaign);

    Campaign updateCampaign(String userId, Campaign campaign);

    void updateApplication(String userId, Application app);

}
