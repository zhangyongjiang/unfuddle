package com.gaoshin.onsalelocal.osl.resource;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.UserDetails;
import com.gaoshin.onsalelocal.osl.facebook.FacebookMe;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.gaoshin.onsalelocal.osl.service.impl.UserServiceImpl;
import common.util.JacksonUtil;
import common.util.web.SpringResourceTester;

public class FacebookLoginTest extends SpringResourceTester {
	@Test
	public void test() throws Exception {
		getBuilder("/ws/user/logout").post(" ");
		
		UserService proxy = getSpringBean(UserService.class);
		UserServiceImpl userService = getTargetObject(proxy, UserServiceImpl.class);
		userService.setFacebookMe(new FacebookMe() {
			@Override
			public Map<String, Object> me(String token) {
				if("kevin".equalsIgnoreCase(token)) {
					String data = "{\"data\":[{\"uid\":628729601,\"name\":\"Kevin Zhang\",\"username\":\"kevin.yj.zhang\",\"email\":null,\"first_name\":\"Kevin\",\"last_name\":\"Zhang\",\"middle_name\":\"\",\"hometown_location\":null,\"current_location\":{\"city\":\"Mountain View\",\"state\":\"California\",\"country\":\"United States\",\"zip\":\"\",\"id\":108212625870265,\"name\":\"Mountain View, California\"},\"pic_small\":\"https:\\/\\/fbcdn-profile-a.akamaihd.net\\/hprofile-ak-ash4\\/203197_628729601_1811472515_t.jpg\",\"pic_big\":\"https:\\/\\/fbcdn-profile-a.akamaihd.net\\/hprofile-ak-ash4\\/203197_628729601_1811472515_n.jpg\",\"pic_square\":\"https:\\/\\/fbcdn-profile-a.akamaihd.net\\/hprofile-ak-ash4\\/203197_628729601_1811472515_q.jpg\",\"pic\":\"https:\\/\\/fbcdn-profile-a.akamaihd.net\\/hprofile-ak-ash4\\/203197_628729601_1811472515_s.jpg\"}]}";
			        try {
				        Map object = JacksonUtil.json2Object(data, JacksonUtil.getTypeRef());
				        List list = (List) object.get("data");
				        return (Map<String, Object>) list.get(0);
			        } catch (Exception e) {
			        	throw new RuntimeException(e);
			        }
				}
				return null;
			}
		});
		
        Account account = new Account();
        String token = "kevin";
        account.setToken(token );
        UserDetails registered = getBuilder("/ws/user/facebook-login").post(UserDetails.class, account);
        Assert.assertEquals("Kevin", registered.getFirstName());
        Assert.assertEquals("Zhang", registered.getLastName());
		
		UserDetails me = getBuilder("/ws/user/me").get(UserDetails.class);
        Assert.assertEquals("Kevin", me.getFirstName());
        Assert.assertEquals("Zhang", me.getLastName());
        System.out.println(JacksonUtil.obj2Json(me));
        
        getBuilder("/ws/user/logout").post(" ");
        UserDetails u2 = getBuilder("/ws/user/facebook-login").post(UserDetails.class, account);
        Assert.assertEquals(registered.getId(), u2.getId());
	}
}
