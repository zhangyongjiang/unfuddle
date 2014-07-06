package com.gaoshin.onsalelocal.osl.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.dao.StoreDao;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.Notification;
import com.gaoshin.onsalelocal.osl.entity.NotificationStatus;
import com.gaoshin.onsalelocal.osl.entity.NotificationType;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.PushStatus;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.service.PushNotificationService;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import common.util.reflection.ReflectionUtil;

@Service("pushNotificationService")
public class PushNotificationServiceImpl implements PushNotificationService {
	@Value("${GoogleAPIAccessServerKey}")
	private String apiKey;
	
	@Value("${apn_certification-file:/shopplus_for_dev_srv.p12}")
	private String certFile = "/shopplus_for_dev_srv.p12";
	
	@Autowired private OslDao oslDao;
	@Autowired private UserDao userDao;
	@Autowired private StoreDao storeDao;
	
	private ApnsService apnService = null;

	@PostConstruct
	public void init() {
		apnService = APNS.newService()
				.withCert(this.getClass().getResourceAsStream(certFile), "1234")
				.withSandboxDestination()
				.build();
	}
	
	@Override
	public void gcmPush(Message message, String regid) throws IOException {
		Sender sender = new Sender(apiKey);
		Result result = sender.send(message, regid, 5);
	}

	@Override
    public void apnPush(String message, String token, Map<String, String>extra, int badge) throws IOException {
		if(extra == null)
			extra = new HashMap<String, String>();
		String payload = APNS.newPayload().alertBody(message).customFields(extra).badge(badge).build();
		apnService.push(token, payload);
	}

	private void push(User user, Notification noti) {
		Account account = userDao.getAccount(user.getId(), AccountType.APN);
		int badge = oslDao.getTotalUnreadNotifications(user.getId());
		if(account != null) {
	    	try {
	    		Map<String, String> extra = new HashMap<String, String>();
	    		extra.put("t", String.valueOf(noti.getType().ordinal()));
	    		extra.put("d", noti.getParam());
                apnPush(noti.getAlert(), account.getExtId(), extra, badge+1);
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		account = userDao.getAccount(user.getId(), AccountType.GCM);
		if(account != null) {
	    	try {
	        	Message.Builder builder = new Message.Builder();
	        	builder.addData("message", noti.getAlert());
	        	builder.addData("t", String.valueOf(noti.getType().ordinal()));
	        	builder.addData("d", noti.getParam());
	    		Message message = builder.build();
                gcmPush(message, account.getExtId());
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
    }
	
	@Override
    public void notifyLikeOffer(String userId, String offerId) {
		Notification noti = new Notification();
		noti.setEventUserId(userId);
		noti.setOfferId(offerId);
		noti.setType(NotificationType.LikeOffer);
		Offer offer = oslDao.getEntity(Offer.class, offerId);
		User user = oslDao.getEntity(User.class, userId);
		String alert = user.getFirstName() + " liked " + offer.getTitle() + ".";
		String param = offerId;
		noti.setAlert(alert);
		noti.setParam(param);
		notify(userId, noti);
    }

	@Override
    public void notifyCommentOffer(String userId, OfferComment offerComment) {
		String offerId = offerComment.getOfferId();
		Notification noti = new Notification();
		noti.setEventUserId(userId);
		noti.setOfferId(offerId);
		noti.setType(NotificationType.CommentOffer);
		Offer offer = oslDao.getEntity(Offer.class, offerId);
		User user = oslDao.getEntity(User.class, userId);
		String alert = user.getFirstName() + " commented " + offer.getTitle() + ".";
		String param = offerId;
		noti.setAlert(alert);
		noti.setParam(param);
		notify(userId, noti);
    }

	@Override
    public void notifyFollowUser(String followerId, String userId) {
		Notification noti = new Notification();
		noti.setEventUserId(followerId);
		noti.setType(NotificationType.FollowUser);
		User following = oslDao.getEntity(User.class, userId);
		User follower = oslDao.getEntity(User.class, followerId);
		String alert = follower.getFirstName() + " followed " + following.getFirstName() + ".";
		String param = userId;
		noti.setAlert(alert);
		noti.setParam(param);
		notify(followerId, noti);
    }

	@Override
    public void notifyFollowStore(String userId, String storeId) {
		Notification noti = new Notification();
		noti.setEventUserId(userId);
		noti.setStoreId(storeId);
		noti.setType(NotificationType.FollowStore);
		Store store = oslDao.getEntity(Store.class, storeId);
		User user = oslDao.getEntity(User.class, userId);
		String alert = user.getFirstName() + " followed " + store.getName() + ".";
		String param = storeId;
		noti.setAlert(alert);
		noti.setParam(param);
		notify(userId, noti);
    }

	@Override
    public void notifyShareOffer(String userId, Offer offer) {
		String offerId = offer.getId();
		Notification noti = new Notification();
		noti.setEventUserId(userId);
		noti.setOfferId(offerId);
		noti.setType(NotificationType.ShareOffer);
		User user = oslDao.getEntity(User.class, userId);
		Store store = oslDao.getEntity(Store.class, offer.getMerchantId());
		String alert = offer.getTitle() + " at " + store.getName() + " - shared by " + user.getFirstName();
		String param = offerId;
		noti.setAlert(alert);
		noti.setParam(param);
		notify(userId, noti);
    }

	private void notify(String userId, Notification noti1) {
		List<User> followers = oslDao.listFollowers(userId);
		for(User u : followers) {
			Notification copy = ReflectionUtil.copy(noti1);
			copy.setUnread(1);
			copy.setUserId(u.getId());
			if(NotificationStatus.Disable.equals(u.getNoti())) {
				copy.setPushStatus(PushStatus.NoNeedToPush);
			}
			else {
				copy.setPushStatus(PushStatus.Unpushed);
				try {
	                push(u, copy);
                } catch (Exception e) {
	                e.printStackTrace();
                }
				copy.setPushStatus(PushStatus.Pushed);
			}
			
			oslDao.insert(copy);
		}
    }
}
