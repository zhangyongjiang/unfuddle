package com.gaoshin.onsalelocal.osl.resource;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.FollowDetailsList;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserDetails;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.gaoshin.onsalelocal.osl.service.impl.UserServiceImpl;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.api.email.MailClient;
import common.api.email.MailMessage;
import common.util.web.ServiceError;

public class UserResourceTest extends OslResourceTester {
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void test() throws Exception {
		User user = new User();
		String login = getCurrentTimeMillisString() + "@osl.com";
		user.setLogin(login);
		String password = getCurrentTimeMillisString();
		user.setPassword(password); 
		
		try {
			User registered = getBuilder("/ws/user/register").post(User.class, user);
			Assert.assertEquals(login, registered.getLogin());
		} catch (UniformInterfaceException e) {
			Assert.assertEquals(ServiceError.InvalidInput.getErrorCode(), e.getResponse().getStatus());
		}
		
		{
			user.setFirstName(getCurrentTimeMillisString());
			user.setLastName(getCurrentTimeMillisString());
			user.setZipcode(getCurrentTimeMillisString());
			User registered = getBuilder("/ws/user/register").post(User.class, user);
			Assert.assertEquals(login, registered.getLogin());
		}
		
		getBuilder("/ws/user/login").post(user);
		UserDetails me = getBuilder("/ws/user/me").get(UserDetails.class);
		Assert.assertEquals(login, me.getLogin());
		Assert.assertNull(me.getPassword());
		
		Assert.assertNull(me.getTempPassword());
		UserService proxy = getSpringBean(UserService.class);
		UserServiceImpl userService = getTargetObject(proxy, UserServiceImpl.class);
		userService.setMailClient(new MailClient() {
			@Override
			public boolean send(MailMessage mail) {
				return false;
			}

			@Override
            public ArrayList<MailMessage> read(boolean delete) {
	            return null;
            }
		});
		
		UserDao userDao = getSpringBean(UserDao.class);
		{
			getBuilder("/ws/user/reset-password/" + login).post();
			User dbuser = userDao.getEntity(User.class, me.getId());
			Assert.assertNotNull(dbuser.getTempPassword());
			
			User tmp = new User();
			tmp.setLogin(login);
			tmp.setPassword(dbuser.getTempPassword());
			getBuilder("/ws/user/login").post(user);
		}

		{
			String androidPnid = getCurrentTimeMillisString();
			getBuilder("/ws/user/register-android-pn-id/" + androidPnid).post();
			Account account = userDao.getAccount(me.getId(), AccountType.GCM);
			Assert.assertEquals(androidPnid, account.getExtId());
		}

		{
			String applePnid = getCurrentTimeMillisString();
			getBuilder("/ws/user/register-ios-pn-id/" + applePnid).post();
			Account account = userDao.getAccount(me.getId(), AccountType.APN);
			Assert.assertEquals(applePnid, account.getExtId());
		}

		String following = getCurrentTimeMillisString();
		getBuilder("/ws/v2/user/follow/" + following).post();
		FollowDetailsList followings = getBuilder("/ws/v2/user/followings").get(FollowDetailsList.class);
		Assert.assertEquals(1, followings.getItems().size());
		Assert.assertEquals(following, followings.getItems().get(0).getUserId());
		
		FollowDetailsList followers = getBuilder("/ws/v2/user/followers").get(FollowDetailsList.class);
		Assert.assertEquals(0, followers.getItems().size());
		
		followers = getBuilder("/ws/v2/user/followers", "userId", following).get(FollowDetailsList.class);
		Assert.assertEquals(1, followers.getItems().size());
		Assert.assertEquals(me.getId(), followers.getItems().get(0).getFollowerId());
	}
}
