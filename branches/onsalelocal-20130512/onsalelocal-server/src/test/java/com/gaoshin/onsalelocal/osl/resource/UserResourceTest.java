package com.gaoshin.onsalelocal.osl.resource;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.gaoshin.onsalelocal.osl.service.impl.UserServiceImpl;
import common.api.email.MailClient;
import common.api.email.MailMessage;
import common.util.web.SpringResourceTester;

public class UserResourceTest extends SpringResourceTester {
	@Test
	public void test() throws Exception {
		User user = new User();
		String login = getCurrentTimeMillisString();
		user.setLogin(login);
		String password = getCurrentTimeMillisString();
		user.setPassword(password);
		User registered = getBuilder("/ws/user/register").post(User.class, user);
		Assert.assertEquals(login, registered.getLogin());
		
		getBuilder("/ws/user/login").post(user);
		User me = getBuilder("/ws/user/me").get(User.class);
		Assert.assertEquals(login, me.getLogin());
		
		Assert.assertNull(me.getTempPassword());
		UserService proxy = getSpringBean(UserService.class);
		UserServiceImpl userService = getTargetObject(proxy, UserServiceImpl.class);
		userService.setMailClient(new MailClient() {
			@Override
			public boolean send(MailMessage mail) {
				return false;
			}
		});
		
		getBuilder("/ws/user/reset-password/" + login).post();
		UserDao userDao = getSpringBean(UserDao.class);
		User dbuser = userDao.getEntity(User.class, me.getId());
		Assert.assertNotNull(dbuser.getTempPassword());

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
	}
}
