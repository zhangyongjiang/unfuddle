package com.gaoshin.appbooster.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.gaoshin.appbooster.bean.ApplicationDetailsList;
import com.gaoshin.appbooster.bean.RewardDetails;
import com.gaoshin.appbooster.bean.RewardDetailsList;
import com.gaoshin.appbooster.bean.UserDetails;
import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.Campaign;
import com.gaoshin.appbooster.entity.Reward;
import com.gaoshin.appbooster.service.PublisherService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/publisher")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class PublisherResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Inject private PublisherService publisherService;
	
    @POST
    @Path("/add-campaign")
    public Campaign addCampaign(Campaign campaign) {
        String userId = getRequesterUserId();
        return publisherService.addCampaign(userId, campaign);
    }
    
    @POST
    @Path("/update-campaign")
    public GenericResponse updateCampaign(Campaign campaign) {
        String userId = getRequesterUserId();
        publisherService.updateCampaign(userId, campaign);
        return new GenericResponse();
    }
    
    @POST
    @Path("/add-application")
    public Application addApplication(Application app) {
        String userId = getRequesterUserId();
        app.setUserId(userId);
        return publisherService.addApplication(app);
    }
    
    @POST
    @Path("/update-application")
    public GenericResponse updateApplication(Application app) {
        String userId = getRequesterUserId();
        publisherService.updateApplication(userId, app);
        return new GenericResponse();
    }
    
    @GET
    @Path("/details")
    public UserDetails getUserDetails(@QueryParam("userId") String userId) {
        if(userId == null || userId.trim().length() == 0) {
            userId = assertRequesterUserId();
        }
        return publisherService.getUserDetails(userId);
    }
    
    @POST
    @Path("/add-reward")
    public Reward addReward(Reward reward) {
        String userId = assertRequesterUserId();
        return publisherService.addReward(userId, reward);
    }
    
    @POST
    @Path("/update-reward")
    public GenericResponse updateReward(Reward reward) {
        String userId = assertRequesterUserId();
        publisherService.updateReward(userId, reward);
        return new GenericResponse();
    }
    
    @POST
    @Path("/remove-reward/{id}")
    public GenericResponse remvoeReward(@PathParam("id") String rewardId) {
        String userId = getRequesterUserId();
        publisherService.removeReward(userId, rewardId);
        return new GenericResponse();
    }
    
    @GET
    @Path("/application/latest")
    public ApplicationDetailsList latestApplications(@QueryParam("size") @DefaultValue("30") int size) {
        return publisherService.latestApplications(size);
    }
    
    @GET
    @Path("/application")
    public Application getApplication(@QueryParam("applicationId") String applicationId) {
        return publisherService.getApplication(applicationId);
    }
    
    @GET
    @Path("/application/reward/latest")
    public RewardDetailsList latestRewards(@QueryParam("size") @DefaultValue("30") int size) {
        return publisherService.latestRewards(size);
    }
    
    @GET
    @Path("/application/reward-details")
    public RewardDetails getRewardDetails(@QueryParam("rewardId") String rewardId) {
        return publisherService.getRewardDetails(rewardId);
    }
}
