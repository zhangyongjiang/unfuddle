package com.gaoshin.onsalelocal.osl.resource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.nextshopper.api.Store;
import com.nextshopper.osl.entity.FavouriteStoreDetailsList;
import com.nextshopper.osl.entity.StoreComment;
import com.nextshopper.osl.entity.StoreCommentDetailsList;
import com.nextshopper.osl.entity.StoreDetails;
import com.nextshopper.osl.entity.StoreDetailsList;
import com.nextshopper.osl.entity.User;
import com.nextshopper.osl.entity.UserList;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.util.web.ServiceError;

public class StoreResourceTest extends OslResourceTester {
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testStore() throws Exception {
		try {
	        createStore();
		} catch (UniformInterfaceException e) {
			Assert.assertEquals(ServiceError.NoGuest.getErrorCode(), e.getResponse().getStatus());
		}
		
		User user = registerNewUser();
		Store store = createStore();
		Assert.assertNotNull(store.getId());
		Assert.assertNotNull(store.getName());
		
		String fileName = "./src/test/resources/coffee.png";
		StoreDetails storeDetails = uploadStoreLogo(store.getId(), fileName);
		
		String imgUrl = storeDetails.getLogo();
		InputStream stream = new URL(imgUrl).openStream();
		byte[] images = IOUtils.toByteArray(stream);
		stream.close();
		
		File file = new File(fileName);
		Assert.assertEquals(file.length(), (long)images.length);
		
		getBuilder("/ws/v2/store/like/" + store.getId()).post();

		FavouriteStoreDetailsList myfavs = getBuilder("/ws/user/my-fav-stores").get(FavouriteStoreDetailsList.class);
		Assert.assertEquals(1, myfavs.getItems().size());
		Assert.assertEquals(store.getName(), myfavs.getItems().get(0).getStore().getName());
		
		StoreDetailsList myoffers = getBuilder("/ws/user/my-stores").get(StoreDetailsList.class);
		Assert.assertEquals(1, myoffers.getItems().size());
		Assert.assertEquals(store.getName(), myoffers.getItems().get(0).getName());

		Store offer2 = createStore();
		getBuilder("/ws/v2/store/like/" + offer2.getId()).post();

		myfavs = getBuilder("/ws/user/my-fav-stores").get(FavouriteStoreDetailsList.class);
		Assert.assertEquals(2, myfavs.getItems().size());
		
		myoffers = getBuilder("/ws/user/my-stores").get(StoreDetailsList.class);
		Assert.assertEquals(2, myoffers.getItems().size());

		UserList userList = getBuilder("/ws/v2/store/like/users", "storeId", store.getId()).get(UserList.class);
		Assert.assertEquals(1, userList.getItems().size());
		Assert.assertEquals(user.getId(), userList.getItems().get(0).getId());

		// second user
		getBuilder("/ws/user/logout").post();
		registerNewUser();
		
		// second user likes offer 1
		getBuilder("/ws/v2/store/like/" + store.getId()).post();
		userList = getBuilder("/ws/v2/store/like/users", "storeId", store.getId()).get(UserList.class);
		Assert.assertEquals(2, userList.getItems().size());
		
		StoreComment comment = new StoreComment();
		String content = getCurrentTimeMillisString();
		comment.setContent(content);
		comment.setStoreId(store.getId());
		getBuilder("/ws/v2/store/comment").post(comment);
		
		StoreCommentDetailsList commentList = getBuilder("/ws/v2/store/comments", "storeId", store.getId()).get(StoreCommentDetailsList.class);
		Assert.assertEquals(1, commentList.getItems().size());
		Assert.assertEquals(comment.getContent(), commentList.getItems().get(0).getContent());
	}
	
	@Test
	public void nearbyStores() {
		
	}
}
