package com.gaoshin.onsalelocal.osl.resource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.nextshopper.api.Offer;
import com.nextshopper.api.OfferDetailsList;
import com.nextshopper.osl.entity.FavouriteOfferDetailsList;
import com.nextshopper.osl.entity.OfferComment;
import com.nextshopper.osl.entity.OfferCommentDetailsList;
import com.nextshopper.osl.entity.User;
import com.nextshopper.osl.entity.UserList;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.util.web.ServiceError;

public class OfferResourceTest extends OslResourceTester {
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testOffer() throws Exception {
		try {
	        Offer offer = createOffer();
		} catch (UniformInterfaceException e) {
			Assert.assertEquals(ServiceError.NoGuest.getErrorCode(), e.getResponse().getStatus());
		}
		
		User user = registerNewUser();
		Offer offer = createOffer();
		Assert.assertNotNull(offer.getTitle());
		
		String imgUrl = offer.getLargeImg();
		InputStream stream = new URL(imgUrl).openStream();
		byte[] images = IOUtils.toByteArray(stream);
		stream.close();
		
		File file = new File("./src/test/resources/coffee.png");
		Assert.assertEquals(file.length(), (long)images.length);
		
		getBuilder("/ws/v2/offer/like/" + offer.getId()).post();

		FavouriteOfferDetailsList myfavs = getBuilder("/ws/user/my-fav-offers").get(FavouriteOfferDetailsList.class);
		Assert.assertEquals(1, myfavs.getItems().size());
		Assert.assertEquals(offer.getTitle(), myfavs.getItems().get(0).getOffer().getTitle());
		
		OfferDetailsList myoffers = getBuilder("/ws/user/my-offers").get(OfferDetailsList.class);
		Assert.assertEquals(1, myoffers.getItems().size());
		Assert.assertEquals(offer.getTitle(), myoffers.getItems().get(0).getTitle());

		Offer offer2 = createOffer();
		getBuilder("/ws/v2/offer/like/" + offer2.getId()).post();

		myfavs = getBuilder("/ws/user/my-fav-offers").get(FavouriteOfferDetailsList.class);
		Assert.assertEquals(2, myfavs.getItems().size());
		
		myoffers = getBuilder("/ws/user/my-offers").get(OfferDetailsList.class);
		Assert.assertEquals(2, myoffers.getItems().size());

		UserList userList = getBuilder("/ws/v2/offer/like/users", "offerId", offer.getId()).get(UserList.class);
		Assert.assertEquals(1, userList.getItems().size());
		Assert.assertEquals(user.getId(), userList.getItems().get(0).getId());

		// second user
		getBuilder("/ws/user/logout").post();
		registerNewUser();
		
		// second user likes offer 1
		getBuilder("/ws/v2/offer/like/" + offer.getId()).post();
		userList = getBuilder("/ws/v2/offer/like/users", "offerId", offer.getId()).get(UserList.class);
		Assert.assertEquals(2, userList.getItems().size());
		
		OfferComment comment = new OfferComment();
		String content = getCurrentTimeMillisString();
		comment.setContent(content);
		comment.setOfferId(offer.getId());
		getBuilder("/ws/v2/offer/comment").post(comment);
		
		OfferCommentDetailsList commentList = getBuilder("/ws/v2/offer/comments", "offerId", offer.getId()).get(OfferCommentDetailsList.class);
		Assert.assertEquals(1, commentList.getItems().size());
		Assert.assertEquals(comment.getContent(), commentList.getItems().get(0).getContent());
	}
}
