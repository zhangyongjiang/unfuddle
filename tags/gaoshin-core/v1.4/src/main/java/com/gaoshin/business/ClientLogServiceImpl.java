package com.gaoshin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.MsgType;
import com.gaoshin.beans.Notification;
import com.gaoshin.beans.NotificationList;
import com.gaoshin.beans.User;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.ClientLogEntity;
import com.gaoshin.entity.UserEntity;
import common.android.log.LogBlock;
import common.android.log.LogList;
import common.util.MD5;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("clientLogService")
@Transactional
public class ClientLogServiceImpl extends BaseServiceImpl implements ClientLogService {
    @Autowired
    private UserDao userDao;

    private boolean checkLog(Notification noti) {
        String md5 = MD5.md5(noti.getMsg());
        return userDao.getFirstEntityBy(ClientLogEntity.class, "sentTime", noti.getSentTime(), "title", md5) != null;
    }

    @Override
    public void save(User user, Notification noti) {
        if (checkLog(noti))
            return;

        noti.setTitle(MD5.md5(noti.getMsg()));
        ClientLogEntity entity = new ClientLogEntity(noti);
        if (user != null) {
            UserEntity ue = userDao.getUser(user);
            entity.setFrom(ue);
        }
        userDao.saveEntity(entity);
    }

    @Override
    public NotificationList list(int start, int size) {
        String sql = "select m from ClientLogEntity m order by m.sentTime desc";
        List<ClientLogEntity> entities = userDao.findEntityBy(sql, start, size);
        NotificationList list = new NotificationList();
        for (ClientLogEntity entity : entities) {
            Notification bean = entity.getBean(Notification.class);
            if (entity.getFrom() != null) {
                bean.setFrom(entity.getFrom().getBean(User.class));
            }
            list.getList().add(bean);
        }
        return list;
    }

    @Override
    public NotificationList list(User user, int start, int size) {
        UserEntity userEntity = userDao.getUser(user);
        if (userEntity == null) {
            throw new BusinessException(ServiceError.InvalidInput);
        }

        String sql = "select m from ClientLogEntity m where and m.from.id = " + userEntity.getId();
        sql += " order by m.sentTime desc";

        List<ClientLogEntity> entities = userDao.findEntityBy(sql, start, size);

        NotificationList list = new NotificationList();
        for (ClientLogEntity entity : entities) {
            Notification bean = entity.getBean(Notification.class);
            if (entity.getFrom() != null) {
                bean.setFrom(entity.getFrom().getBean(User.class));
            }
            list.getList().add(bean);
        }

        return list;
    }

    @Override
    public void save(User user, LogList logs) {
        for (LogBlock block : logs.getList()) {
            Notification noti = new Notification();
            noti.setType(MsgType.Log);
            noti.setMsg(block.toString());
            noti.setSentTime(block.getList().get(0).getTime());
            save(user, noti);
        }
    }

}
