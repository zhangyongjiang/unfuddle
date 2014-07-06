package com.gaoshin.business;

import com.gaoshin.beans.Notification;
import com.gaoshin.beans.NotificationList;
import com.gaoshin.beans.User;
import common.android.log.LogList;

public interface ClientLogService {
    void save(User user, Notification noti);

    NotificationList list(int start, int size);

    NotificationList list(User user, int start, int size);

    void save(User user, LogList logs);
}
