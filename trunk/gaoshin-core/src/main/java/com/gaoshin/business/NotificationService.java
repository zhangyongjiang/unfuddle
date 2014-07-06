package com.gaoshin.business;

import com.gaoshin.beans.NotificationList;
import com.gaoshin.beans.User;

public interface NotificationService {

    NotificationList after(User user, Long after, int size, boolean markRead, boolean unreadOnly);

    NotificationList before(User user, Long before, int size);
}
