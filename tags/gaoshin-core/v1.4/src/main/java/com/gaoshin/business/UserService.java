package com.gaoshin.business;

import java.io.InputStream;
import java.io.OutputStream;

import com.gaoshin.beans.DimensionValueList;
import com.gaoshin.beans.StringList;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserAndDistanceList;
import com.gaoshin.beans.UserDevice;
import com.gaoshin.beans.UserFileList;
import com.gaoshin.beans.UserFileType;
import com.gaoshin.beans.UserList;

public interface UserService {

    User signup(User user);

    User login(User user);

    User update(User user);

    User getUser(User user);

    DimensionValueList listUserDimensions(User user);

    UserAndDistanceList search(User excludeUser, float latitude, float longitude, Float latitude1, Float longitude1,
            Float miles, Long minAge,
            Long maxAge,
            String dims, int offset,
            int size);

    void setUserDimensions(User user, DimensionValueList dimensionValues);

    DimensionValueList getUserDimensions(User user);

    void upload(User user, InputStream is, String name, UserFileType resType);

    void setIcon(User user, String name);

    User checkUser(User user);

    void addFriend(User user, Long friendId);

    UserList getFriends(User user);

    void banUser(Long userId);

    void unbanUser(Long userId);

    void download(User user, UserFileType resType, String name, OutputStream outputStream);

    void downloadPhoto(Long resId, OutputStream outputStream, int width);

    UserList getMostRecents();

    StringList listUserFiles(User user, String folder);

    UserDevice getUserDeviceInfo(Long id);

    void deleteUser(Long userId);

    void confirmDeletion(User user);

    void upgradeAlbum();

    UserFileList listUserResources(UserFileType resType, Long userId);

    void download(Long fid, OutputStream outputStream);

}
