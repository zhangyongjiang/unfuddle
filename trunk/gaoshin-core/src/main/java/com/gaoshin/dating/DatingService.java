package com.gaoshin.dating;

import com.gaoshin.beans.Gender;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserAndDistanceList;

public interface DatingService {

    User signup(DatingUser user);

    void migrate();

    DatingProfile getUserProfile(Long userId);

    void saveIntroduction(User user, String intro);

    void saveGender(User user, Gender gender);

    void saveBirthYear(User user, int birthYear);

    void saveHeight(User user, int centerMeter);

    void saveWeight(User user, int kg);

    void saveLookingFor(User user, DimensionLookingFor lookingFor);

    void saveInterest0(User user, String interest0);

    void saveInterest1(User user, String interest1);

    void saveInterest2(User user, String interest2);

    void saveRace(User user, DimensionRace race);

    void saveJob(User user, DimensionJob job);

    void saveIncome(User user, DimensionIncome income);

    void saveSmoking(User user, DimensionSmoking smoking);

    void saveDrinking(User user, DimensionDrinking drinking);

    void saveLanguage0(User user, DimensionLanguage language0);

    void saveLanguage1(User user, DimensionLanguage language1);

    void saveLikeChildren(User user, DimensionLikeChildren likeChildren);

    void saveNumOfKids(User user, int numOfKids);

    void savePets(User user, DimensionPets pets);

    void saveMarriageStatus(User user, DimensionMarriageStatus marriageStatus);

    void saveEducation(User user, DimensionEducation education);

    SearchCriteria saveSearchCriteria(User user, SearchCriteria criteria);

    SearchCriteriaList getSearchCriteria(User user);

    UserAndDistanceList search(SearchCriteria searchCriteria, Float latitude, Float longitude,
            Float latitude1, Float longitude1,
            Float miles);

}
