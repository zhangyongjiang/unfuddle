package com.gaoshin.appbooster.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gaoshin.appbooster.bean.AppBoosterClassTrees;
import com.gaoshin.appbooster.bean.ApplicationDetails;
import com.gaoshin.appbooster.bean.ApplicationDetailsList;
import com.gaoshin.appbooster.bean.RewardDetails;
import com.gaoshin.appbooster.bean.RewardDetailsList;
import com.gaoshin.appbooster.bean.UserDetails;
import com.gaoshin.appbooster.dao.PublisherDao;
import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.ApplicationType;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Repository
public class PublisherDaoImpl extends GenericDaoImpl implements PublisherDao {
    
    @Override
    public Application getApplication(ApplicationType type, String marketId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("marketId", marketId);
        params.put("type", type);
        return getUniqueResult(Application.class, params);
    }

    @Override
    public List<Application> listUserApplications(String userId) {
        return query(Application.class, Collections.singletonMap("userId", userId));
    }

    @Override
    public UserDetails getUserDetails(String userId) {
        String sql = "select * from User where id=:id";
        List detailsAndChildren = getDetailsAndChildren(AppBoosterClassTrees.getUserClassTree(), sql, Collections.singletonMap("id", userId));
        if(detailsAndChildren.size() == 0)
            throw new BusinessException(ServiceError.NotFound);
        return (UserDetails) detailsAndChildren.get(0);
    }

    @Override
    public ApplicationDetailsList latestApplications(int size) {
        ApplicationDetailsList result = new ApplicationDetailsList();
        String sql = "select * from Application order by created desc limit " + size;
        List<ApplicationDetails> detailsList = getDetailsAndChildren(AppBoosterClassTrees.getApplicationClassTree(), sql, null);
        result.setItems(detailsList);
        return result;
    }

    @Override
    public RewardDetailsList latestRewards(int size) {
        RewardDetailsList result = new RewardDetailsList();
        String sql = "select * from Reward order by created desc limit " + size;
        List detailsList = getDetailsAndParents(AppBoosterClassTrees.getRewardClassTree(), sql, null);
        result.getItems().addAll(detailsList);
        return result;
    }

    @Override
    public RewardDetails getRewardDetails(String rewardId) {
        String sql = "select * from Reward where id=:id ";
        List detailsList = getDetailsAndParents(AppBoosterClassTrees.getRewardClassTree(), sql, Collections.singletonMap("id", rewardId));
        if(detailsList.size() == 0)
            throw new BusinessException(ServiceError.NotFound);
        return (RewardDetails) detailsList.get(0);
    }
}
