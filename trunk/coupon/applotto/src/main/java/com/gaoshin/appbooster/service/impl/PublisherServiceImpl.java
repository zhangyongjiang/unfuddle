package com.gaoshin.appbooster.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.appbooster.bean.AppBoosterClassTrees;
import com.gaoshin.appbooster.bean.ApplicationDetails;
import com.gaoshin.appbooster.bean.ApplicationDetailsList;
import com.gaoshin.appbooster.bean.ApplicationList;
import com.gaoshin.appbooster.bean.CampaignDetails;
import com.gaoshin.appbooster.bean.RewardDetails;
import com.gaoshin.appbooster.bean.RewardDetailsList;
import com.gaoshin.appbooster.bean.UserDetails;
import com.gaoshin.appbooster.dao.PublisherDao;
import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.ApplicationStatus;
import com.gaoshin.appbooster.entity.Campaign;
import com.gaoshin.appbooster.entity.Reward;
import com.gaoshin.appbooster.service.PublisherService;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class PublisherServiceImpl extends ServiceBase implements PublisherService {
    @Autowired private PublisherDao publisherDao;
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Application addApplication(Application app) {
        long now = System.currentTimeMillis();
        app.setId(null);
        app.setCreated(now);
        app.setUpdated(now);
        app.setStatus(ApplicationStatus.Approved);
        if(app.getUserId() == null || app.getType() == null || app.getMarketId() == null)
            throw new BusinessException(ServiceError.InvalidInput);
        Application existing = publisherDao.getApplication(app.getType(), app.getMarketId());
        if(existing != null)
            throw new BusinessException(ServiceError.Duplicated);
        publisherDao.insert(app);
        return app;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Reward addReward(String userId, Reward reward) {
        CampaignDetails campaignDetails = (CampaignDetails) publisherDao.getDetailsAndParents(AppBoosterClassTrees.getCampaignClassTree(), reward.getCampaignId());
        if(campaignDetails == null)
            throw new BusinessException(ServiceError.NotFound);
        if(!campaignDetails.getApplicationDetails().getUserDetails().getId().equals(userId))
            throw new BusinessException(ServiceError.NotAuthorized);
        reward.setId(null);
        publisherDao.insert(reward);
        return reward;
    }

    @Override
    public ApplicationList listUserApplications(String userId) {
        ApplicationList result = new ApplicationList();
        result.setItems(publisherDao.listUserApplications(userId));
        return result;
    }

    @Override
    public UserDetails getUserDetails(String userId) {
        return publisherDao.getUserDetails(userId);
    }

    @Override
    public ApplicationDetailsList latestApplications(int size) {
        return publisherDao.latestApplications(size);
    }

    @Override
    public RewardDetailsList latestRewards(int size) {
        return publisherDao.latestRewards(size);
    }

    @Override
    public Application getApplication(String applicationId) {
        return publisherDao.getEntity(Application.class, applicationId, "*");
    }

    @Override
    public RewardDetails getRewardDetails(String rewardId) {
        return publisherDao.getRewardDetails(rewardId);
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Reward updateReward(String userId, Reward reward) {
        RewardDetails rewardDetails = (RewardDetails) publisherDao.getDetailsAndParents(AppBoosterClassTrees.getRewardClassTree(), reward.getId());
        if(rewardDetails == null)
            throw new BusinessException(ServiceError.NotFound);
        if(!rewardDetails.getCampaignDetails().getApplicationDetails().getUserDetails().getId().equals(userId))
            throw new BusinessException(ServiceError.NotAuthorized);
        publisherDao.updateEntity(reward);
        return reward;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void removeReward(String userId, String rewardId) {
        RewardDetails rewardDetails = (RewardDetails) publisherDao.getDetailsAndParents(AppBoosterClassTrees.getRewardClassTree(), rewardId);
        if(rewardDetails == null)
            throw new BusinessException(ServiceError.NotFound);
        if(!rewardDetails.getCampaignDetails().getApplicationDetails().getUserDetails().getId().equals(userId))
            throw new BusinessException(ServiceError.NotAuthorized);
        publisherDao.delete(Reward.class, Collections.singletonMap("id", rewardId));
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Campaign addCampaign(String userId, Campaign campaign) {
        if(campaign.getApplicationId() == null)
            throw new BusinessException(ServiceError.InvalidInput);
        ApplicationDetails appDetails = (ApplicationDetails) publisherDao.getDetailsAndParents(AppBoosterClassTrees.getApplicationClassTree(), campaign.getApplicationId());
        if(appDetails == null)
            throw new BusinessException(ServiceError.NotFound, "cannot find application for id " + campaign.getApplicationId());
        if(!appDetails.getUserId().equals(userId))
            throw new BusinessException(ServiceError.NotAuthorized, "user " + userId + " does not own application " + campaign.getApplicationId());
        publisherDao.insert(campaign);
        return campaign;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Campaign updateCampaign(String userId, Campaign campaign) {
        if(campaign.getId() == null)
            throw new BusinessException(ServiceError.InvalidInput);
        CampaignDetails campaignDetails = (CampaignDetails) publisherDao.getDetailsAndParents(AppBoosterClassTrees.getCampaignClassTree(), campaign.getId());
        if(campaignDetails == null)
            throw new BusinessException(ServiceError.NotFound, "cannot find campaign for id " + campaign.getId());
        if(!campaignDetails.getApplicationDetails().getUserId().equals(userId))
            throw new BusinessException(ServiceError.NotAuthorized, "user " + userId + " does not own campaign " + campaign.getId());
        campaign.setApplicationId(campaignDetails.getApplicationId());
        publisherDao.updateEntity(campaign);
        return campaign;
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void updateApplication(String userId, Application app) {
        Application indb = publisherDao.getEntity(Application.class, app.getId());
        if(indb == null)
            throw new BusinessException(ServiceError.NotFound, "cannot find applicaiton " + app.getId() + " " + app.getName());
        if(!indb.getUserId().equals(userId))
            throw new BusinessException(ServiceError.NotAuthorized, "Application " + app.getId() + " " + app.getName() + " does not belong to user " + userId);
        app.setStatus(indb.getStatus());
        publisherDao.updateEntity(app);
    }
}
