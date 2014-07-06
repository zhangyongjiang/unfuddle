package com.gaoshin.dating;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.Gender;
import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.StringHolder;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserAndDistanceList;
import com.gaoshin.webservice.GaoshinResource;
import com.sun.jersey.spi.inject.Inject;
import common.web.BusinessException;
import common.web.ServiceError;

@Path("/dating")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class DatingResource extends GaoshinResource {
    @Inject
    private DatingService datingService;

    @POST
    @Path("signup")
    public User signup(DatingUser user) {
        return datingService.signup(user);
    }

    @GET
    @Path("migrate")
    public GenericResponse migrate() {
        User user = assertUser();
        if (!user.isSuperUser()) {
            throw new BusinessException(ServiceError.Forbidden);
        }
        datingService.migrate();
        return new GenericResponse();
    }

    @GET
    @Path("profile/{uid}")
    public DatingProfile getProfile(@PathParam("uid") Long userId) {
        return datingService.getUserProfile(userId);
    }

    @GET
    @Path("profile")
    public DatingProfile getMyProfile() {
        User user = assertUser();
        return datingService.getUserProfile(user.getId());
    }

    @POST
    @Path("profile/edit-intro")
    public GenericResponse editIntro(StringHolder data) {
        User user = assertUser();
        datingService.saveIntroduction(user, data.getValue());
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-gender")
    public GenericResponse editGender(StringHolder data) {
        User user = assertUser();
        Gender gender = Gender.valueOf(data.getValue());
        datingService.saveGender(user, gender);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-lookingfor")
    public GenericResponse editLookingFor(StringHolder data) {
        User user = assertUser();
        DimensionLookingFor lookingFor = DimensionLookingFor.valueOf(data.getValue());
        datingService.saveLookingFor(user, lookingFor);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-race")
    public GenericResponse editRace(StringHolder data) {
        User user = assertUser();
        DimensionRace race = DimensionRace.valueOf(data.getValue());
        datingService.saveRace(user, race);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-job")
    public GenericResponse editJob(StringHolder data) {
        User user = assertUser();
        DimensionJob job = DimensionJob.valueOf(data.getValue());
        datingService.saveJob(user, job);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-income")
    public GenericResponse editIncome(StringHolder data) {
        User user = assertUser();
        DimensionIncome income = DimensionIncome.valueOf(data.getValue());
        datingService.saveIncome(user, income);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-smoking")
    public GenericResponse editSmoking(StringHolder data) {
        User user = assertUser();
        DimensionSmoking smoking = DimensionSmoking.valueOf(data.getValue());
        datingService.saveSmoking(user, smoking);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-drinking")
    public GenericResponse editDrinking(StringHolder data) {
        User user = assertUser();
        DimensionDrinking drinking = DimensionDrinking.valueOf(data.getValue());
        datingService.saveDrinking(user, drinking);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-language0")
    public GenericResponse editLanguage0(StringHolder data) {
        User user = assertUser();
        DimensionLanguage language0 = DimensionLanguage.valueOf(data.getValue());
        datingService.saveLanguage0(user, language0);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-language1")
    public GenericResponse editLanguage1(StringHolder data) {
        User user = assertUser();
        DimensionLanguage language1 = DimensionLanguage.valueOf(data.getValue());
        datingService.saveLanguage1(user, language1);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-like-kids")
    public GenericResponse editLikeKids(StringHolder data) {
        User user = assertUser();
        DimensionLikeChildren likeChildren = DimensionLikeChildren.valueOf(data.getValue());
        datingService.saveLikeChildren(user, likeChildren);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-like-pets")
    public GenericResponse editPets(StringHolder data) {
        User user = assertUser();
        DimensionPets pets = DimensionPets.valueOf(data.getValue());
        datingService.savePets(user, pets);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-marriage")
    public GenericResponse editMarriage(StringHolder data) {
        User user = assertUser();
        DimensionMarriageStatus marriageStatus = DimensionMarriageStatus.valueOf(data.getValue());
        datingService.saveMarriageStatus(user, marriageStatus);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-birthyear")
    public GenericResponse editBirthYear(StringHolder data) {
        User user = assertUser();
        int birthYear = Integer.parseInt(data.getValue());
        datingService.saveBirthYear(user, birthYear);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-num-of-kids")
    public GenericResponse editNumOfKids(StringHolder data) {
        User user = assertUser();
        int numOfKids = Integer.parseInt(data.getValue());
        datingService.saveNumOfKids(user, numOfKids);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-height")
    public GenericResponse editHeight(StringHolder data) {
        User user = assertUser();
        int centerMeter = Integer.parseInt(data.getValue());
        datingService.saveHeight(user, centerMeter);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-weight")
    public GenericResponse editWeight(StringHolder data) {
        User user = assertUser();
        int kg = Integer.parseInt(data.getValue());
        datingService.saveWeight(user, kg);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-interest0")
    public GenericResponse editInterest0(StringHolder data) {
        User user = assertUser();
        String interest0 = data.getValue();
        datingService.saveInterest0(user, interest0);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-interest1")
    public GenericResponse editInterest1(StringHolder data) {
        User user = assertUser();
        String interest1 = data.getValue();
        datingService.saveInterest1(user, interest1);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-interest2")
    public GenericResponse editInterest2(StringHolder data) {
        User user = assertUser();
        String interest2 = data.getValue();
        datingService.saveInterest2(user, interest2);
        return new GenericResponse();
    }

    @POST
    @Path("profile/edit-education")
    public GenericResponse editEducation(StringHolder data) {
        User user = assertUser();
        DimensionEducation education = DimensionEducation.valueOf(data.getValue());
        datingService.saveEducation(user, education);
        return new GenericResponse();
    }

    @POST
    @Path("search/criteria/save")
    public SearchCriteria saveSearchCriteria(SearchCriteria criteria) {
        User user = assertUser();
        criteria = datingService.saveSearchCriteria(user, criteria);
        return criteria;
    }

    @GET
    @Path("search/criteria/list")
    public SearchCriteriaList getSearchCriteria() {
        User user = assertUser();
        SearchCriteriaList criteria = datingService.getSearchCriteria(user);
        if (criteria.getList().isEmpty()) {
            SearchCriteria def = new SearchCriteria();
            DatingProfile profile = getMyProfile();

            DimensionLookingFor lookingFor = profile.getLookingFor();
            if (lookingFor != null) {
                if (lookingFor.equals(DimensionLookingFor.Man)) {
                    def.setGender(new Gender[] { Gender.Man });
                } else if (lookingFor.equals(DimensionLookingFor.Woman)) {
                    def.setGender(new Gender[] { Gender.Woman });
                }
            }

            def.setMinBirthYear(profile.getBirthYear() - 5);
            def.setMaxBirthYear(profile.getBirthYear() + 5);

            def = saveSearchCriteria(def);
            criteria.getList().add(def);
        }
        return criteria;
    }

    @GET
    @Path("search")
    public UserAndDistanceList search(
            @QueryParam("lat") Float latitude, @QueryParam("lng") Float longitude,
            @QueryParam("lat1") Float latitude1, @QueryParam("lng1") Float longitude1,
            @DefaultValue("3000") @QueryParam("miles") Float miles
            ) {
        SearchCriteriaList list = getSearchCriteria();
        if (list.getList().size() == 0) {
            throw new BusinessException(ServiceError.InvalidInput);
        }
        if (latitude == null || longitude == null) {
            User user = getUser();
            if (user != null && user.getCurrentLocation() != null) {
                latitude = user.getCurrentLocation().getLatitude();
                longitude = user.getCurrentLocation().getLongitude();
            }
        }

        return datingService.search(list.getList().get(0), latitude, longitude, latitude1, longitude1, miles);
    }
}
