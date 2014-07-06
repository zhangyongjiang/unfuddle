package com.gaoshin.dating;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Gender;
import com.gaoshin.beans.LocationDistance;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserAndDistance;
import com.gaoshin.beans.UserAndDistanceList;
import com.gaoshin.business.BaseServiceImpl;
import com.gaoshin.business.ObjectService;
import com.gaoshin.business.PhoneInfo;
import com.gaoshin.business.UserService;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.DimensionValueEntity;
import com.gaoshin.entity.UserDimensionValueEntity;
import com.gaoshin.entity.UserEntity;
import common.util.DesEncrypter;
import common.util.GeoUtil;
import common.util.GeoUtil.GeoRange;
import common.util.JacksonUtil;
import common.util.JsonUtil;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("datingService")
@Transactional
public class DatingServiceImpl extends BaseServiceImpl implements DatingService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ObjectService objectService;

    @Override
    public User signup(DatingUser datingUser) {
        this.verifyBeans(datingUser);

        User user = new User();
        user.setName(datingUser.getName());
        user.setPhone(datingUser.getPhone());
        user.setClientType(datingUser.getClientType());
        user.setInterests(datingUser.getInterests());
        user.setPassword(datingUser.getPassword());
        user.setGender(datingUser.getGender());
        user.setBirthYear(datingUser.getBirthyear());

        String deviceInfo = datingUser.getDeviceInfo();
        if (deviceInfo != null) {
            try {
                deviceInfo = DesEncrypter.selfdecrypt(deviceInfo).get(0);
                PhoneInfo pi = JsonUtil.toJavaObject(deviceInfo, PhoneInfo.class);
                user.setDeviceInfo(pi);
            } catch (Exception e) {
            }
        }

        User dbuser = userService.signup(user);
        
        DatingProfileEntity profile = new DatingProfileEntity();
        profile.setUser(userDao.getUser(dbuser));
        profile.setLookingFor(datingUser.getLookingfor());
        profile.setBirthYear(datingUser.getBirthyear());
        profile.setGender(datingUser.getGender());
        userDao.saveEntity(profile);
        
        return dbuser;
    }

    @Override
    public void migrate() {
        List<UserEntity> allUsers = userDao.findEntityBy(UserEntity.class, new HashMap<String, Object>());
        for (UserEntity ue : allUsers) {
            DatingProfileEntity profile = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", ue.getId());
            if (profile != null) {
                continue;
            }
            profile = new DatingProfileEntity();
            profile.setUser(ue);
            profile.setBirthYear(ue.getBirthYear());
            profile.setGender(ue.getGender());
            String interests = ue.getInterests();
            if (interests != null) {
                String[] split = interests.trim().split("[\n\r]+");
                if (split.length > 0) {
                    String string = split[0];
                    profile.setInterest0(string);
                }
                if (split.length > 1) {
                    profile.setInterest1(split[1]);
                }
                if (split.length > 2) {
                    profile.setInterest2(split[2]);
                }
            }

            List<UserDimensionValueEntity> entities = userDao.findEntityBy(UserDimensionValueEntity.class, "userEntity.id", ue.getId());
            for (UserDimensionValueEntity entity : entities) {
                if (entity.getDimensionEntity().getName().equals("Looking For")) {
                    Long dimValueId = entity.getDimvalue();
                    DimensionValueEntity valueEntity = userDao.getEntity(DimensionValueEntity.class, dimValueId);
                    if (valueEntity.getDvalue().equals("Man")) {
                        profile.setLookingFor(DimensionLookingFor.Man);
                    } else if (valueEntity.getDvalue().equals("Woman")) {
                        profile.setLookingFor(DimensionLookingFor.Woman);
                    } else if (valueEntity.getDvalue().equals("Friends")) {
                        profile.setLookingFor(DimensionLookingFor.Friends);
                    }
                }
            }

            userDao.saveEntity(profile);
        }
    }

    @Override
    public DatingProfile getUserProfile(Long userId) {
        return userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userId).getBean(DatingProfile.class);
    }

    @Override
    public void saveIntroduction(User user, String intro) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setIntroduction(intro);
            userDao.saveEntity(entity);
        } else {
            entity.setIntroduction(intro);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveGender(User user, Gender gender) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setGender(gender);
            userDao.saveEntity(entity);

            userEntity.setGender(gender);
            userDao.saveEntity(userEntity);
        } else {
            entity.setGender(gender);
            userDao.saveEntity(entity);

            userEntity.setGender(gender);
            userDao.saveEntity(userEntity);
        }
    }

    @Override
    public void saveBirthYear(User user, int birthYear) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setBirthYear(birthYear);
            userDao.saveEntity(entity);
            userEntity.setBirthYear(birthYear);
            userDao.saveEntity(userEntity);
        } else {
            entity.setBirthYear(birthYear);
            userDao.saveEntity(entity);
            userEntity.setBirthYear(birthYear);
            userDao.saveEntity(userEntity);
        }
    }

    @Override
    public void saveHeight(User user, int centerMeter) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setHeight(centerMeter);
            userDao.saveEntity(entity);
        } else {
            entity.setHeight(centerMeter);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveWeight(User user, int kg) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setWeight(kg);
            userDao.saveEntity(entity);
        } else {
            entity.setWeight(kg);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveLookingFor(User user, DimensionLookingFor lookingFor) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setLookingFor(lookingFor);
            userDao.saveEntity(entity);
        } else {
            entity.setLookingFor(lookingFor);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveInterest0(User user, String interest0) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setInterest0(interest0);
            userDao.saveEntity(entity);
        } else {
            entity.setInterest0(interest0);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveInterest1(User user, String interest1) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setInterest1(interest1);
            userDao.saveEntity(entity);
        } else {
            entity.setInterest1(interest1);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveInterest2(User user, String interest2) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setInterest2(interest2);
            userDao.saveEntity(entity);
        } else {
            entity.setInterest2(interest2);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveRace(User user, DimensionRace race) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setRace(race);
            userDao.saveEntity(entity);
        } else {
            entity.setRace(race);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveJob(User user, DimensionJob job) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setJob(job);
            userDao.saveEntity(entity);
        } else {
            entity.setJob(job);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveIncome(User user, DimensionIncome income) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setIncome(income);
            userDao.saveEntity(entity);
        } else {
            entity.setIncome(income);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveSmoking(User user, DimensionSmoking smoking) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setSmoking(smoking);
            userDao.saveEntity(entity);
        } else {
            entity.setSmoking(smoking);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveDrinking(User user, DimensionDrinking drinking) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setDrinking(drinking);
            userDao.saveEntity(entity);
        } else {
            entity.setDrinking(drinking);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveLanguage0(User user, DimensionLanguage language0) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setLanguage0(language0);
            userDao.saveEntity(entity);
        } else {
            entity.setLanguage0(language0);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveLanguage1(User user, DimensionLanguage language1) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setLanguage1(language1);
            userDao.saveEntity(entity);
        } else {
            entity.setLanguage1(language1);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveLikeChildren(User user, DimensionLikeChildren likeChildren) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setLikekids(likeChildren);
            userDao.saveEntity(entity);
        } else {
            entity.setLikekids(likeChildren);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveNumOfKids(User user, int numOfKids) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setKids(numOfKids);
            userDao.saveEntity(entity);
        } else {
            entity.setKids(numOfKids);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void savePets(User user, DimensionPets pets) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setPets(pets);
            userDao.saveEntity(entity);
        } else {
            entity.setPets(pets);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveMarriageStatus(User user, DimensionMarriageStatus marriageStatus) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setMarriage(marriageStatus);
            userDao.saveEntity(entity);
        } else {
            entity.setMarriage(marriageStatus);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public void saveEducation(User user, DimensionEducation education) {
        UserEntity userEntity = userDao.getUser(user);
        DatingProfileEntity entity = userDao.getFirstEntityBy(DatingProfileEntity.class, "user.id", userEntity.getId());
        if (entity == null) {
            entity = new DatingProfileEntity();
            entity.setUser(userEntity);
            entity.setEducation(education);
            userDao.saveEntity(entity);
        } else {
            entity.setEducation(education);
            userDao.saveEntity(entity);
        }
    }

    @Override
    public SearchCriteria saveSearchCriteria(User user, SearchCriteria criteria) {
        UserEntity userEntity = userDao.getUser(user);
        if (criteria.getId() != null) {
            SearchCriteriaEntity entity = userDao.getEntity(SearchCriteriaEntity.class, criteria.getId());
            if (!entity.getUser().getId().equals(user.getId()))
                throw new BusinessException(ServiceError.Forbidden);
            entity.setCriteria(JacksonUtil.obj2Json(criteria));
            userDao.saveEntity(entity);
        } else {
            SearchCriteriaEntity entity = new SearchCriteriaEntity();
            entity.setUser(userEntity);
            entity.setCriteria(JacksonUtil.obj2Json(criteria));
            userDao.saveEntity(entity);
            criteria.setId(entity.getId());
        }

        return criteria;
    }

    @Override
    public SearchCriteriaList getSearchCriteria(User user) {
        List<SearchCriteriaEntity> entities = userDao.findEntityBy(SearchCriteriaEntity.class, "user.id", user.getId());
        SearchCriteriaList list = new SearchCriteriaList();
        for (SearchCriteriaEntity e : entities) {
            SearchCriteria bean = JacksonUtil.jsonStr2Object(e.getCriteria(), SearchCriteria.class);
            bean.setId(e.getId());
            list.getList().add(bean);
        }
        return list;
    }

    @Override
    public UserAndDistanceList search(SearchCriteria searchCriteria,
            Float latitude, Float longitude,
            Float latitude1, Float longitude1,
            Float miles
            ) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sql = new StringBuilder(
                "select u.id as id, " +
                        "u.phone as phone, " +
                        "u.username as name, " +
                        "u.icon as icon, " +
                        "p.gender as gender, " +
                        "p.byear as byear, " +
                        "loc.latitude as latitude, " +
                        "loc.longitude as longitude, " +
                        "loc.city as city, " +
                        "loc.state as state " +
                " from USERS u , user_location loc, xo_profile p ");
        sql.append(" where u.role!='BADGUY' and u.role!='DELETED' and u.id = loc.user_id and loc.id = u.current_loc and u.id = p.user_id ");

        append(sql, "p.race", searchCriteria.getRace());
        append(sql, "p.gender", searchCriteria.getGender());
        append(sql, "p.drinking", searchCriteria.getDrinking());
        append(sql, "p.education", searchCriteria.getEducation());
        append(sql, "p.income", searchCriteria.getIncome());
        append(sql, "p.language0", searchCriteria.getLanguage());
        append(sql, "p.language1", searchCriteria.getLanguage());
        append(sql, "p.marriage", searchCriteria.getMarriage());
        append(sql, "p.smoking", searchCriteria.getSmoking());
        append(sql, "p.likekids", searchCriteria.getLikekids());
        append(sql, "p.pets", searchCriteria.getPets());

        if (searchCriteria.getMinBirthYear() != null && searchCriteria.getMinBirthYear().intValue() > 0) {
            sql.append(" and p.byear>=").append(searchCriteria.getMinBirthYear());
        }
        if (searchCriteria.getMaxBirthYear() != null && searchCriteria.getMaxBirthYear().intValue() > 0) {
            sql.append(" and p.byear<=").append(searchCriteria.getMaxBirthYear());
        }

        if (searchCriteria.getMaxKids() != null) {
            sql.append(" and p.kids<=").append(searchCriteria.getMaxKids());
        }

        if (searchCriteria.getMinHeight() != null && searchCriteria.getMinHeight().intValue() > 0) {
            sql.append(" and p.height>=").append(searchCriteria.getMinHeight());
        }
        if (searchCriteria.getMaxHeight() != null && searchCriteria.getMaxHeight().intValue() > 0) {
            sql.append(" and p.height<=").append(searchCriteria.getMaxHeight());
        }

        if (searchCriteria.getMinWeight() != null && searchCriteria.getMinWeight().intValue() > 0) {
            sql.append(" and p.weight>=").append(searchCriteria.getMinWeight());
        }
        if (searchCriteria.getMaxWeight() != null && searchCriteria.getMaxWeight().intValue() > 0) {
            sql.append(" and p.weight<=").append(searchCriteria.getMaxWeight());
        }

        if (latitude != null && longitude != null) {
            if (latitude1 != null) {
                sql.append(" and loc.latitude > ").append(Math.min(latitude, latitude1));
                sql.append(" and loc.latitude < ").append(Math.max(latitude, latitude1));
                sql.append(" and loc.longitude > ").append(Math.min(longitude, longitude1));
                sql.append(" and loc.longitude < ").append(Math.max(longitude, longitude1));
                sql.append(" order by (abs(loc.latitude - ").append(latitude).append(")*1000000) * (abs(loc.longitude - ").append(longitude).append(")*1000000)");
            } else if (miles != null) {
                GeoRange range = GeoUtil.getRange(latitude, longitude, miles);
                sql.append(" and loc.latitude > ").append(range.getMinLat());
                sql.append(" and loc.latitude < ").append(range.getMaxLat());
                sql.append(" and loc.longitude>").append(range.getMinLng());
                sql.append(" and loc.longitude < ").append(range.getMaxLng());
                sql.append(" order by (abs(loc.latitude - ").append(latitude).append(")*1000000) * (abs(loc.longitude - ").append(longitude).append(")*1000000)");
            }
        }
        

        int offset = 0;
        int size = 100;
        String query = sql.toString();
        List<Object[]> records = userDao.nativeQuerySelect(query, offset, size);

        UserAndDistanceList list = new UserAndDistanceList();
        for (Object[] row : records) {
            int index = 0;
            UserAndDistance user = new UserAndDistance();
            list.getList().add(user);

            user.setId(Long.parseLong(row[index].toString()));
            index++;

            user.setPhone(row[index] == null ? null : row[index].toString());
            index++;

            user.setName(row[index] == null ? null : row[index].toString());
            index++;

            user.setIcon(row[index] == null ? null : row[index].toString());
            index++;

            user.setGender(row[index] == null ? null : getEnum(Gender.class, Integer.parseInt(row[index].toString())));
            index++;

            user.setBirthYear(row[index] == null ? null : Integer.parseInt(row[index].toString()));
            index++;

            LocationDistance location = new LocationDistance();
            user.setLocationDistance(location);
            float latitude2 = Float.parseFloat(row[index].toString());
            index++;

            float longitude2 = Float.parseFloat(row[index].toString());
            location.setLatitude(latitude2);
            location.setLongitude(longitude2);
            Double distance = null;
            try {
                distance = GeoUtil.distance(latitude, longitude, latitude2, longitude2);
            } catch (Exception e) {
            }
            location.setDistance(distance);
            index++;

            location.setCity(row[index] == null ? null : row[index].toString());
            index++;

            location.setState(row[index] == null ? null : row[index].toString());
            index++;
        }
        
        if (latitude != null && longitude != null) {
            Collections.sort(list.getList(), new Comparator<UserAndDistance>() {
                @Override
                public int compare(UserAndDistance o1, UserAndDistance o2) {
                    if (o1 == null && o2 == null)
                        return 0;
                    if (o1 == null && o2 != null)
                        return 1;
                    if (o1 != null && o2 == null)
                        return -1;
                    return (int) (o1.getLocationDistance().getDistance() - o2.getLocationDistance().getDistance());
                }
            });
        }

        return list;
    }

    private void append(StringBuilder sql, String column, Enum[] criterias) {
        if (criterias != null && criterias.length > 0) {
            sql.append(" and (");
            boolean first = true;
            for (Enum race : criterias) {
                if (first) {
                    first = false;
                } else {
                    sql.append(" or ");
                }
                sql.append(column).append(" = ").append(race.ordinal());
            }
            sql.append(" ) ");
        }
    }

    private <T extends Enum> T getEnum(Class<T> clazz, int ordinal) {
        int index = 0;
        for (T e : clazz.getEnumConstants()) {
            if (index == ordinal)
                return e;
            index++;
        }
        throw new RuntimeException("not found for " + ordinal + " from enum  " + clazz);
    }
}
