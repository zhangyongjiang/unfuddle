package com.gaoshin.onsalelocal.osl.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gaoshin.onsalelocal.osl.service.PushNotificationService;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

@Service("pushNotificationService")
public class PushNotificationServiceImpl implements PushNotificationService {
	@Value("${GoogleAPIAccessServerKey}")
	private String apiKey;

	@Override
	public void gcmPush(Message message, String regid) throws IOException {
		Sender sender = new Sender(apiKey);
		Result result = sender.send(message, regid, 5);
	}

	@Override
    public void apnPush(String message, String token, String cerfile) throws IOException {
		ApnsService service = APNS.newService()
				.withCert(cerfile, "r3dmine!")
				.withSandboxDestination()
				.build();
		String payload = APNS.newPayload().alertBody(message).build();
		service.push(token, payload);
	}
}
