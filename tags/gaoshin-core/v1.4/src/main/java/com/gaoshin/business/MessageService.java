package com.gaoshin.business;

import com.gaoshin.beans.Message;
import com.gaoshin.beans.MessageList;
import com.gaoshin.beans.User;

public interface MessageService {

    Message send(User user, Message msg);

    MessageList list(User user1, Long userId2, Long since, int start, int size);

}
