package com.gaoshin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Notification;
import com.gaoshin.beans.NotificationList;
import com.gaoshin.beans.User;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.NotificationEntity;
import com.gaoshin.entity.UserEntity;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl extends BaseServiceImpl implements NotificationService {
    @Autowired
    private UserDao userDao;

    @Override
    public NotificationList after(User user, Long after, int size, boolean markRead, boolean unreadOnly) {
        UserEntity userEntity = userDao.getUser(user);
        if (userEntity == null) {
            throw new BusinessException(ServiceError.InvalidInput, (user == null ? null : (user.getPhone() + "," + user.getId())));
        }

        String sql = "select m from NotificationEntity m where m.id>?1 and m.to.id = ?2";
        if (unreadOnly)
            sql += " and m.read = ?3";
        sql += " order by m.sentTime";

        List<NotificationEntity> entities = null;
        if (!unreadOnly)
            entities = userDao.findEntityBy(sql, 0, size, after, userEntity.getId());
        else
            entities = userDao.findEntityBy(sql, 0, size, after, userEntity.getId(), false);

        NotificationList list = new NotificationList();
        for (NotificationEntity entity : entities) {
            Notification bean = entity.getBean(Notification.class);
            list.getList().add(bean);
        }

        if (markRead) {
            sql = "update notification set is_read = 1 where to_id = " + userEntity.getId() + " and id <= " + after;
            userDao.nativeQueryUpdate(sql);
        }

        return list;
    }

    @Override
    public NotificationList before(User user, Long since, int size) {
        return null;
    }

}
