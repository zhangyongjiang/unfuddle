package com.gaoshin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Message;
import com.gaoshin.beans.MessageList;
import com.gaoshin.beans.MsgType;
import com.gaoshin.beans.User;
import com.gaoshin.dao.MessageDao;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.MessageEntity;
import com.gaoshin.entity.NotificationEntity;
import com.gaoshin.entity.UserEntity;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("messageService")
@Transactional
public class MessageServiceImpl extends BaseServiceImpl implements MessageService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Override
    public Message send(User user, Message msg) {
        UserEntity userEntity = userDao.getUser(user);
        User receipt = User.decryptId(msg.getReceipt());
        UserEntity receitpEntity = userDao.getUser(receipt);
        MessageEntity entity = new MessageEntity();
        entity.setContent(msg.getContent());
        entity.setSender(userEntity);
        entity.setReceipt(receitpEntity);
        messageDao.saveEntity(entity);
        msg.setId(entity.getId());

        NotificationEntity gm = new NotificationEntity();
        gm.setType(MsgType.Web);
        gm.setSubtype(MsgType.Message);
        String path = "/m/message/send/index.jsp.oo?to=" + userEntity.getId();
        gm.setUrl(path);
        gm.setTitle("You've got message from " + userEntity.getName());
        gm.setMsg(msg.getContent());
        gm.setFrom(userEntity);
        gm.setTo(receitpEntity);
        gm.setSentTime(System.currentTimeMillis());
        gm.setRead(false);
        messageDao.saveEntity(gm);

        return entity.getBean(Message.class);
    }

    @Override
    public MessageList list(User user1, Long userId2, Long since, int start, int size) {
        Long userId1 = user1.getId();
        if (userId1 == null) {
            UserEntity userEntity1 = userDao.getUser(user1);
            if (userEntity1 == null) {
                throw new BusinessException(ServiceError.InvalidInput);
            }
            userId1 = userEntity1.getId();
        }

        String sql = "select m from MessageEntity m where m.id>?1 and ((m.sender.id = ?2 and m.receipt.id=?3) or (m.sender.id = ?4 and m.receipt.id=?5)) order by m.createTime desc";
        List<MessageEntity> entities = messageDao.findEntityBy(sql, start, size, since, userId1, userId2, userId2, userId1);
        MessageList list = new MessageList();
        for (MessageEntity entity : entities) {
            Message bean = entity.getBean(Message.class);
            bean.setSender(entity.getSender().getId() + "");
            bean.setReceipt(entity.getReceipt().getId() + "");
            list.getList().add(bean);
        }
        return list;
    }

}
