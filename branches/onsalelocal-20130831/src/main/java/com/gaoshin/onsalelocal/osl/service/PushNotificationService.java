package com.gaoshin.onsalelocal.osl.service;

import java.io.IOException;

import com.google.android.gcm.server.Message;

public interface PushNotificationService {
	void gcmPush(Message message, String regid) throws IOException;
	void apnPush(String message, String token) throws IOException;
}
