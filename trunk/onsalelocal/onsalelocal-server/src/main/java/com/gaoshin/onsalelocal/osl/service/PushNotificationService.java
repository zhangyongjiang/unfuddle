package com.gaoshin.onsalelocal.osl.service;

import java.io.IOException;
import java.util.Map;

import com.google.android.gcm.server.Message;
import com.nextshopper.api.Offer;
import com.nextshopper.osl.entity.OfferComment;

public interface PushNotificationService {
	void gcmPush(Message message, String regid) throws IOException;
	void apnPush(String message, String token, Map<String, String>extra, int badge) throws IOException;
	void notifyLikeOffer(String userId, String offerId);
	void notifyCommentOffer(String userId, OfferComment offerComment);
	void notifyFollowUser(String followerId, String userId);
	void notifyFollowStore(String userId, String storeId);
	void notifyShareOffer(String userId, Offer offer);
}
