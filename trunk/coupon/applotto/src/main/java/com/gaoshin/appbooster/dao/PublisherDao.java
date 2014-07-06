package com.gaoshin.appbooster.dao;

import java.util.List;

import com.gaoshin.appbooster.bean.ApplicationDetailsList;
import com.gaoshin.appbooster.bean.RewardDetails;
import com.gaoshin.appbooster.bean.RewardDetailsList;
import com.gaoshin.appbooster.bean.UserDetails;
import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.ApplicationType;

public interface PublisherDao extends GenericDao {
    Application getApplication(ApplicationType type, String marketId);

    List<Application> listUserApplications(String userId);

    UserDetails getUserDetails(String userId);

    ApplicationDetailsList latestApplications(int size);

    RewardDetailsList latestRewards(int size);

    RewardDetails getRewardDetails(String rewardId);
}
