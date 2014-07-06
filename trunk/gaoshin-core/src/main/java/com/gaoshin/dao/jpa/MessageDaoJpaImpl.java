package com.gaoshin.dao.jpa;

import org.springframework.stereotype.Repository;

import com.gaoshin.dao.MessageDao;

@Repository("messageDao")
public class MessageDaoJpaImpl extends DaoComponentImpl implements MessageDao {
}
