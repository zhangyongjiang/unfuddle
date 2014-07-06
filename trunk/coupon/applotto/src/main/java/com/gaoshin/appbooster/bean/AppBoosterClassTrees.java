package com.gaoshin.appbooster.bean;

import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.Campaign;
import com.gaoshin.appbooster.entity.Reward;
import com.gaoshin.appbooster.entity.User;
import common.util.db.ClassTree;

public class AppBoosterClassTrees {
    private static ClassTree UserClassTree = new ClassTree(User.class, null);
    private static ClassTree ApplicationClassTree = new ClassTree(Application.class, UserClassTree);
    private static ClassTree CampaignClassTree = new ClassTree(Campaign.class, ApplicationClassTree);
    private static ClassTree RewardClassTree = new ClassTree(Reward.class, CampaignClassTree);
    static {
        UserClassTree.setChild(ApplicationClassTree);
        ApplicationClassTree.setChild(CampaignClassTree);
        CampaignClassTree.setChild(RewardClassTree);
    }

    public static ClassTree getUserClassTree() {
        return UserClassTree;
    }

    public static ClassTree getApplicationClassTree() {
        return ApplicationClassTree;
    }

    public static ClassTree getCampaignClassTree() {
        return CampaignClassTree;
    }

    public static ClassTree getRewardClassTree() {
        return RewardClassTree;
    }

}
