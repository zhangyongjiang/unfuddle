package com.gaoshin.dating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.beans.DimensionValueList;
import com.gaoshin.beans.User;
import com.gaoshin.business.BaseServiceImpl;
import com.gaoshin.business.ObjectService;
import com.gaoshin.business.PhoneInfo;
import com.gaoshin.business.UserService;
import common.util.DesEncrypter;
import common.util.JsonUtil;

@Service("datingService")
@Transactional
public class DatingServiceImpl extends BaseServiceImpl implements DatingService {
    @Autowired
    private UserService userService;

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

        DimensionValueList userDims = new DimensionValueList();
        Dimension lookingForDimension = objectService.getDimensionByName("Looking For");
        for (DimensionValue dv : lookingForDimension.getValues()) {
            if (dv.getDvalue().equals(datingUser.getLookingfor().name())) {
                dv.setDimension(lookingForDimension);
                userDims.getList().add(dv);
            }
        }
        
        if(userDims.getList().size()>0) {
            userService.setUserDimensions(dbuser, userDims);
        }
        
        return dbuser;
    }

}
