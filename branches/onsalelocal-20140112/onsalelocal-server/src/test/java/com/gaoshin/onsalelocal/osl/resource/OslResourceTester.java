package com.gaoshin.onsalelocal.osl.resource;

import java.io.File;
import java.math.BigDecimal;
import java.util.Random;

import javax.ws.rs.core.MediaType;

import org.junit.Before;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import common.util.web.SpringResourceTester;

public class OslResourceTester extends SpringResourceTester {
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	protected User registerNewUser() {
		User user = new User();
		String login = getCurrentTimeMillisString() + "@osl.com";
		user.setLogin(login);
		
		String password = getCurrentTimeMillisString();
		user.setPassword(password);
		
		user.setFirstName(getCurrentTimeMillisString());
		user.setLastName(getCurrentTimeMillisString());
		user.setZipcode(getCurrentTimeMillisString());
		
		User registered = getBuilder("/ws/user/register").post(User.class, user);
		registered.setPassword(user.getPassword());
		return registered;
	}

	protected Offer createOffer() throws Exception {
		FormDataMultiPart part = new FormDataMultiPart();

		String fileName = "./src/test/resources/coffee.png";
		part.bodyPart(new FileDataBodyPart("image", new File(fileName), MediaType.APPLICATION_OCTET_STREAM_TYPE));

		String title = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("title", title));
		
		String price = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("price", price));
		
		String discount = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("discount", discount));
		
		String description = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("description", description));
		
		String merchant = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("merchant", merchant));
		
		String address = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("address", address));
		
		String city = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("city", city));
		
		String state = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("state", state));
		
		String country = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("country", country));
		
		String phone = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("phone", phone));
		
		String tags = getCurrentTimeMillisString();
		part.bodyPart(new FormDataBodyPart("tags", tags));
		
		part.bodyPart(new FormDataBodyPart("latitude", "37.0"));
		part.bodyPart(new FormDataBodyPart("longitude", "-122.0"));
		
        WebResource webResource = getWebResource("/ws/v2/offer");
		Builder builder = webResource.header("Content-type", MediaType.MULTIPART_FORM_DATA_TYPE).accept("application/json;charset=utf-8");
		Offer offer = builder.post(Offer.class, part);
		
		return offer;
	}
	
	protected Store createStore() throws Exception {
		Store store = new Store();
		store.setAddress(getCurrentTimeMillisString());
		store.setCity(getCurrentTimeMillisString());
		store.setState(getCurrentTimeMillisString());
		store.setCountry(getCurrentTimeMillisString());
		store.setZipcode(getCurrentTimeMillisString());
		store.setName(getCurrentTimeMillisString());
		store.setPhone(getCurrentTimeMillisString());
		store.setLatitude(new BigDecimal(37f + new Random().nextFloat() / 10f));
		store.setLongitude(new BigDecimal(-122f + new Random().nextFloat() / 10f));
		
		Store created = getBuilder("/ws/store/create").post(Store.class, store);
		return created;
	}
	
	protected StoreDetails uploadStoreLogo(String storeId, String fileName) {
		FormDataMultiPart part = new FormDataMultiPart();
		part.bodyPart(new FileDataBodyPart("image", new File(fileName), MediaType.APPLICATION_OCTET_STREAM_TYPE));
		part.bodyPart(new FormDataBodyPart("storeId", storeId));
        WebResource webResource = getWebResource("/ws/store/image");
		Builder builder = webResource.header("Content-type", MediaType.MULTIPART_FORM_DATA_TYPE).accept("application/json;charset=utf-8");
		StoreDetails store = builder.post(StoreDetails.class, part);
		return store;
	}
}
