package com.gaoshin.business;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.ConfKeyList;
import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.beans.DimensionValueList;
import com.gaoshin.beans.Gender;
import com.gaoshin.beans.Location;
import com.gaoshin.beans.LocationDistance;
import com.gaoshin.beans.MsgType;
import com.gaoshin.beans.StringList;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserAndDistance;
import com.gaoshin.beans.UserAndDistanceList;
import com.gaoshin.beans.UserDevice;
import com.gaoshin.beans.UserFile;
import com.gaoshin.beans.UserFileList;
import com.gaoshin.beans.UserFileType;
import com.gaoshin.beans.UserList;
import com.gaoshin.beans.UserRole;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.DimensionEntity;
import com.gaoshin.entity.DimensionValueEntity;
import com.gaoshin.entity.FriendEntity;
import com.gaoshin.entity.NotificationEntity;
import com.gaoshin.entity.UserDeviceEntity;
import com.gaoshin.entity.UserDimensionValueEntity;
import com.gaoshin.entity.UserEntity;
import com.gaoshin.entity.UserResourceEntity;
import common.util.GeoUtil;
import common.util.GeoUtil.GeoRange;
import common.util.MD5;
import common.util.Misc;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Value("${app.home}")
    private String appHome;

    @Override
    public User signup(User user) {
        user.setRole(UserRole.USER);
        verifyBeans(user, "signup");
        if (userDao.getUser(user) != null)
            throw new BusinessException(ServiceError.Duplicated, "User name or phone is taken.");

        user.setName(Misc.removeTag(user.getName()));
        user.setInterests(Misc.removeTag(user.getInterests()));

        UserEntity entity = new UserEntity(user);
        entity.setPassword(MD5.md5(user.getPassword()));
        userDao.saveEntity(entity);

        if (user.getDeviceInfo() != null) {
            UserDeviceEntity ude = new UserDeviceEntity(user.getDeviceInfo());
            ude.setUserEntity(entity);
            userDao.saveEntity(ude);
        }

        NotificationEntity gm = new NotificationEntity();
        gm.setType(MsgType.Web);
        gm.setSubtype(MsgType.Message);
        String path = "/m/thanks/index.jsp.oo";
        gm.setUrl(path);
        gm.setTitle("Thanks for downloading XO");
        gm.setMsg("Hope you like it!");
        gm.setFrom(null);
        gm.setTo(entity);
        gm.setSentTime(System.currentTimeMillis());
        gm.setRead(false);
        userDao.saveEntity(gm);

        return entity.getBean(User.class);
    }

    @Override
    public User update(User user) {
        UserEntity userEntity = userDao.getUser(user);
        if (userEntity == null)
            throw new BusinessException(ServiceError.InvalidInput, "User doesn't exist.");
        if (user.getName() != null)
            userEntity.setName(user.getName());
        userEntity.setIntroduction(user.getIntroduction());
        userEntity.setInterests(user.getInterests());
        userDao.saveEntity(userEntity);
        return userEntity.getBean(User.class);
    }

    @Override
    public User login(User user) {
        verifyBeans(user, "login");
        UserEntity entity = userDao.getUser(user);
        if (entity == null)
            throw new BusinessException(ServiceError.NotFound);
        if (!entity.isRightPassword(user.getPassword()))
            throw new BusinessException(ServiceError.InvalidInput);
        User bean = entity.getBean(User.class);
        if (entity.getCurrentLocation() != null)
            bean.setCurrentLocation(entity.getCurrentLocation().getBean(Location.class));
        return bean;
    }

    @Override
    public User getUser(User user) {
        UserEntity userEntity = userDao.getUser(user);
        User bean = userEntity.getBean(User.class);
        if (userEntity.getCurrentLocation() != null)
            bean.setCurrentLocation(userEntity.getCurrentLocation().getBean(Location.class));

        user.setProfiles(listUserDimensions(user));
        return bean;
    }

    @Override
    public User checkUser(User user) {
        UserEntity userEntity = userDao.getUser(user);
        if (userEntity == null)
            return null;
        User bean = userEntity.getBean(User.class);
        if (userEntity.getCurrentLocation() != null)
            bean.setCurrentLocation(userEntity.getCurrentLocation().getBean(Location.class));
        return bean;
    }

    @Override
    public DimensionValueList listUserDimensions(User user) {
        List<UserDimensionValueEntity> entities = null;
        if (user.getId() != null)
            entities = userDao.findEntityBy(UserDimensionValueEntity.class, "userEntity.id", user.getId());
        else if (user.getPhone() != null)
            entities = userDao.findEntityBy(UserDimensionValueEntity.class, "userEntity.phone", user.getPhone());
        else
            throw new BusinessException(ServiceError.InvalidInput);
        DimensionValueList list = new DimensionValueList();
        for (UserDimensionValueEntity entity : entities) {
            DimensionValue dimensionValue = new DimensionValue();
            dimensionValue.setDimvalue(entity.getDimvalue());

            for (DimensionValueEntity dve : entity.getDimensionEntity().getDimensionValues()) {
                if (dve.getId().equals(entity.getDimvalue())) {
                    dimensionValue.setDvalue(dve.getDvalue());
                }
            }

            dimensionValue.setDimension(entity.getDimensionEntity().getBean(Dimension.class));
            list.getList().add(dimensionValue);
        }
        return list;
    }

    @Override
    public UserAndDistanceList search(User excludeUser,
            float latitude, float longitude,
            Float latitude1, Float longitude1,
            Float miles, Long minAge,
            Long maxAge,
            final String gender,
            int offset, int size) {
        if (excludeUser != null && excludeUser.getId() == null) {
            UserEntity userEntity = userDao.getUser(excludeUser);
            if (userEntity != null) {
                excludeUser.setId(userEntity.getId());
            }
        }
        Long excludeUserId = (excludeUser == null) ? null : excludeUser.getId();
        StringBuilder sql = new StringBuilder("select u.id as id, u.phone as phone, u.username as name, u.icon as icon, u.interests as interestes, u.gender as gender, u.byear as byear, " +
                "loc.latitude as latitude, loc.longitude as longitude, loc.city as city, loc.state as state " +
                " from USERS u , user_location loc, xo_profile p ");

        sql.append(" where u.role!='BADGUY' and u.role!='DELETED' and u.id = loc.user_id and loc.id = u.current_loc and u.id = p.user_id ");

        if (excludeUserId != null) {
            sql.append(" and u.id != ").append(excludeUserId);
        }

        if (gender != null) {
            try {
                Gender genum = Gender.valueOf(gender);
                sql.append(" and p.gender = '").append(genum.name()).append("' ");
            } catch (Exception e) {
                System.out.println("invalid gender: " + gender);
            }
        }

        if (minAge != null) {
            sql.append(" and p.byear >= " + minAge);
        }
        if (maxAge != null) {
            sql.append(" and p.byear <= " + maxAge);
        }

        if (latitude1 != null) {
            sql.append(" and loc.latitude > ").append(Math.min(latitude, latitude1));
            sql.append(" and loc.latitude < ").append(Math.max(latitude, latitude1));
            sql.append(" and loc.longitude > ").append(Math.min(longitude, longitude1));
            sql.append(" and loc.longitude < ").append(Math.max(longitude, longitude1));
        } else if (miles != null) {
            GeoRange range = GeoUtil.getRange(latitude, longitude, miles);
            sql.append(" and loc.latitude > ").append(range.getMinLat());
            sql.append(" and loc.latitude < ").append(range.getMaxLat());
            sql.append(" and loc.longitude>").append(range.getMinLng());
            sql.append(" and loc.longitude < ").append(range.getMaxLng());
        }


        // sql.append(" order by (abs(loc.latitude - ").append(latitude).append(")*1000000) * (abs(loc.longitude - ").append(longitude).append(")*1000000)");

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

            user.setInterests(row[index] == null ? null : row[index].toString());
            index++;

            user.setGender(row[index] == null ? null : Gender.valueOf(row[index].toString()));
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
            double distance = GeoUtil.distance(latitude, longitude, latitude2, longitude2);
            location.setDistance(distance);
            index++;

            location.setCity(row[index] == null ? null : row[index].toString());
            index++;

            location.setState(row[index] == null ? null : row[index].toString());
            index++;
        }

        if (gender != null) {
            List<UserAndDistance> same = new ArrayList<UserAndDistance>();
            List<UserAndDistance> different = new ArrayList<UserAndDistance>();
            for (UserAndDistance uad : list.getList()) {
                if (gender.equals(uad.getGender().name())) {
                    same.add(uad);
                } else {
                    different.add(uad);
                }
            }
            Collections.sort(same, new Sorter());
            Collections.sort(different, new Sorter());
            list.getList().clear();
            list.getList().addAll(same);
            list.getList().addAll(different);
        } else {
            Collections.sort(list.getList(), new Sorter());
        }

        list.setMiles(miles.intValue());
        return list;
    }

    private static int getBirthYear(int age) {
        return Calendar.getInstance().get(Calendar.YEAR) - age;
    }

    private static class Sorter implements Comparator<UserAndDistance> {
        @Override
        public int compare(UserAndDistance o1, UserAndDistance o2) {
            if (o1 == null && o2 == null)
                return 0;
            if (o1 == null && o2 != null)
                return 1;
            if (o1 != null && o2 == null)
                return -1;
            return o1.getLocationDistance().getDistance() > o2.getLocationDistance().getDistance() ? 1 : -1;
        }
    }

    @Override
    public void setUserDimensions(User user, DimensionValueList dimensionValues) {
        UserEntity userEntity = userDao.getUser(user);
        userDao.nativeQueryUpdate("delete from user_dim where user_id = " + userEntity.getId());
        for (DimensionValue dv : dimensionValues.getList()) {
            if (dv.getDimvalue() != null) {
                UserDimensionValueEntity entity = new UserDimensionValueEntity();
                entity.setUserEntity(userEntity);
                entity.setDimvalue(dv.getDimvalue());
                DimensionEntity dimEntity = userDao.getEntity(DimensionEntity.class, dv.getDimension().getId());
                entity.setDimensionEntity(dimEntity);
                userDao.saveEntity(entity);
            }
        }
    }

    @Override
    public DimensionValueList getUserDimensions(User user) {
        DimensionValueList dimensionValues = new DimensionValueList();
        UserEntity userEntity = userDao.getUser(user);
        List<UserDimensionValueEntity> entities = userDao.findEntityBy(UserDimensionValueEntity.class, "userEntity.id", userEntity.getId());
        for (UserDimensionValueEntity entity : entities) {
            DimensionValue bean = new DimensionValue();
            bean.setDimvalue(entity.getDimvalue());
            bean.setDimension(entity.getDimensionEntity().getBean(Dimension.class));
            dimensionValues.getList().add(bean);
        }
        return dimensionValues;
    }

    @Override
    public void upload(User user, InputStream is, String name, UserFileType resType) {
        try {
            UserEntity userEntity = userDao.getUser(user);
            String folder = resType.name();
            FileOutputStream fos = new FileOutputStream(getUserFilePath(userEntity, true, folder, name));
            byte[] buff = new byte[8192];
            while (true) {
                int len = is.read(buff);
                if (len < 0)
                    break;
                fos.write(buff, 0, len);
            }
            fos.close();

            UserResourceEntity ure = new UserResourceEntity();
            ure.setUserEntity(userEntity);
            ure.setCreateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            ure.setMimetype(null);
            ure.setName(name);
            ure.setType(resType);
            ure.setMimetype("image/" + name.substring(name.lastIndexOf('.') + 1));
            ure.setPath(folder);
            userDao.saveEntity(ure);
        } catch (IOException e) {
            throw new BusinessException(ServiceError.System, e.getMessage());
        }
    }

    @Override
    public void setIcon(User user, String name) {
        UserEntity userEntity = userDao.getUser(user);
        userEntity.setIcon(name);
        userDao.saveEntity(userEntity);
    }

    private String getUserHome(Long userId, boolean create) {
        String path = appHome + "/" + userId;
        File f = new File(path);
        if (!f.exists() && create) {
            f.mkdirs();
        }
        return path;
    }

    private String getUserFilePath(UserEntity user, boolean create, String folder, String name) {
        String path = null;
        if ("/".equals(folder))
            path = getUserHome(user.getId(), create) + folder;
        else if (folder.startsWith("/"))
            path = getUserHome(user.getId(), create) + folder + "/";
        else
            path = getUserHome(user.getId(), create) + "/" + folder + "/";
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        path = path + name;
        return path;
    }

    @Override
    public void addFriend(User user, Long friendId) {
        UserEntity userEntity = userDao.getUser(user);
        UserEntity friendUserEntity = userDao.getEntity(UserEntity.class, friendId);
        if (friendUserEntity == null)
            throw new BusinessException(ServiceError.InvalidInput);

        List<FriendEntity> friends = userDao.findEntityBy(FriendEntity.class, "user.id", userEntity.getId(), "friend.id", friendUserEntity.getId());
        if (friends.size() > 0) {
            throw new BusinessException(ServiceError.Duplicated, "Friend " + friendUserEntity.getName() + " is already in the friend list");
        }

        FriendEntity entity = new FriendEntity();
        entity.setUser(userEntity);
        entity.setFriend(friendUserEntity);
        userDao.saveEntity(entity);
    }

    @Override
    public UserList getFriends(User user) {
        UserEntity userEntity = userDao.getUser(user);
        String sql = "select * from USERS where id in ( select friend_id from friends where user_id=" + userEntity.getId() + ")";
        List<UserEntity> entities = userDao.nativeQuerySelect(sql, UserEntity.class);
        UserList list = new UserList();
        for (UserEntity entity : entities) {
            list.getList().add(entity.getBean(User.class));
        }
        return list;
    }

    private void setClientConf(UserEntity to, UserEntity from, String key, String value) {
        NotificationEntity gm = new NotificationEntity();
        gm.setType(MsgType.Conf);
        gm.setSubtype(MsgType.Update);
        gm.setTitle(key);
        gm.setMsg(value);
        gm.setTo(to);
        gm.setFrom(from);
        gm.setSentTime(System.currentTimeMillis());
        gm.setRead(false);
        userDao.saveEntity(gm);
    }

    @Override
    public void banUser(Long userId) {
        UserEntity user = userDao.getEntity(UserEntity.class, userId);
        user.setRole(UserRole.BADGUY);
        userDao.saveEntity(user);
        setClientConf(user, null, ConfKeyList.RemoveCookies.name(), "true");
    }

    @Override
    public void unbanUser(Long userId) {
        UserEntity user = userDao.getEntity(UserEntity.class, userId);
        user.setRole(UserRole.USER);
        userDao.saveEntity(user);
        setClientConf(user, null, ConfKeyList.RemoveCookies.name(), "true");
    }

    @Override
    public void download(User user, UserFileType resType, String name, OutputStream outputStream) {
        try {
            UserEntity userEntity = userDao.getUser(user);
            FileInputStream fis = new FileInputStream(getUserFilePath(userEntity, false, resType.name(), name));
            byte[] buff = new byte[8192];
            while (true) {
                int len = fis.read(buff);
                if (len < 0)
                    break;
                outputStream.write(buff, 0, len);
            }
            fis.close();
        } catch (IOException e) {
            throw new BusinessException(ServiceError.System, e.getMessage());
        }
    }

    @Override
    public void downloadPhoto(Long resId, OutputStream outputStream, int newWidth) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            download(resId, os);
            ByteArrayInputStream fis = new ByteArrayInputStream(os.toByteArray());

            Image image = (Image) ImageIO.read(fis);
            double scale = (double) newWidth / (double) image.getWidth(null);
            int newHeight = (int) ((double) image.getHeight(null) * scale);

            // Draw the scaled image
            BufferedImage resizedImage = new BufferedImage(newWidth,
                    newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);
            graphics2D.dispose();

            ImageIO.write(resizedImage, "png", outputStream);
        } catch (IOException e) {
            throw new BusinessException(ServiceError.System, e.getMessage());
        }
    }

    @Override
    public UserList getMostRecents() {
        String sql = "select * from USERS order by regtime desc";
        List<UserEntity> entities = userDao.nativeQuerySelect(sql, UserEntity.class, 0, 10);
        UserList list = new UserList();
        for (UserEntity entity : entities) {
            User bean = entity.getBean(User.class);
            if (entity.getCurrentLocation() != null)
                bean.setCurrentLocation(entity.getCurrentLocation().getBean(Location.class));
            list.getList().add(bean);
        }
        return list;
    }

    @Override
    public StringList listUserFiles(User user, String folder) {
        StringList list = new StringList();
        String path = getUserHome(user.getId(), false) + folder;
        File dir = new File(path);
        if (!dir.exists()) {
            return list;
        }
        String[] fileNames = dir.list();
        for (String s : fileNames) {
            list.getList().add(s);
        }
        return list;
    }

    @Override
    public UserDevice getUserDeviceInfo(Long userid) {
        UserDeviceEntity deviceEntity = userDao.getFirstEntityBy(UserDeviceEntity.class, "userEntity.id", userid);
        return deviceEntity == null ? null : deviceEntity.getBean(UserDevice.class);
    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity user = userDao.getEntity(UserEntity.class, userId);
        NotificationEntity gm = new NotificationEntity();
        gm.setType(MsgType.Web);
        gm.setSubtype(MsgType.Message);
        String path = "/m/user/confirm-deletion/index.jsp.oo";
        gm.setUrl(path);
        gm.setTitle("Bye");
        gm.setMsg("Thanks for using XO!");
        gm.setFrom(null);
        gm.setTo(user);
        gm.setSentTime(System.currentTimeMillis());
        gm.setRead(false);
        userDao.saveEntity(gm);
    }

    @Override
    public void confirmDeletion(User user) {
        UserEntity entity = userDao.getUser(user);
        entity.setRole(UserRole.DELETED);
        entity.setPhone(entity.getPhone() + "_" + entity.getId());
        entity.setName(entity.getName() + "_" + entity.getId());
        userDao.saveEntity(entity);
    }

    @Override
    public void upgradeAlbum() {
        String sql = "select * from USERS";
        String path = "/album";
        List<UserEntity> users = userDao.nativeQuerySelect(sql, UserEntity.class);
        for (UserEntity entity : users) {
            StringList files = listUserFiles(entity.getBean(User.class), path);
            for (String fname : files.getList()) {
                List<UserResourceEntity> list = userDao.findEntityBy(UserResourceEntity.class, "userEntity.id", entity.getId(), "name", fname);
                if (list.size() == 0) {
                    UserResourceEntity ure = new UserResourceEntity();
                    ure.setUserEntity(entity);
                    ure.setCreateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
                    ure.setMimetype(null);
                    ure.setName(fname);
                    ure.setType(UserFileType.album);
                    ure.setMimetype("image/" + fname.substring(fname.lastIndexOf('.') + 1));
                    ure.setPath(path);
                    userDao.saveEntity(ure);
                }
            }
        }
    }

    @Override
    public UserFileList listUserResources(UserFileType resType, Long userId) {
        List<UserResourceEntity> list = userDao.findEntityBy(UserResourceEntity.class, "userEntity.id", userId, "type", resType);
        UserFileList ret = new UserFileList();
        for (UserResourceEntity entity : list) {
            UserFile userFile = entity.getBean(UserFile.class);
            if (entity.getUserEntity() != null)
                userFile.setUser(entity.getUserEntity().getBean(User.class));
            ret.getList().add(userFile);
        }
        return ret;
    }

    @Override
    public void download(Long fid, OutputStream outputStream) {
        UserResourceEntity entity = userDao.getEntity(UserResourceEntity.class, fid);
        String filePath = getUserFilePath(entity.getUserEntity(), false, entity.getType().name(), entity.getName());
        try {
            FileInputStream fis = new FileInputStream(filePath);
            byte[] buff = new byte[8192];
            while (true) {
                int len = fis.read(buff);
                if (len < 0)
                    break;
                outputStream.write(buff, 0, len);
            }
            fis.close();
        } catch (IOException e) {
            throw new BusinessException(ServiceError.System);
        }
    }
}
