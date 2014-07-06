package com.gaoshin.onsaleflyer.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;

import com.gaoshin.onsaleflyer.beans.Poster;
import com.gaoshin.onsaleflyer.beans.PosterId;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.beans.UserAsset;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class AssetResourceTest extends LoginUserResourceTester {
	public static final String HOME = "./target/osf-home";

	private Poster poster;

	@Test
	public void test() {
	}

	public void testAll() throws IOException {
		createPoster();
		File file = new File("./src/test/resources/coffee.png");
		upload(file, poster.getOwner().getPosterId());
		
		UserAsset ua = new UserAsset(poster.getOwner(), "coffee.png");
		File uploaded = new File(ua.getRealPath(HOME));
		Assert.assertTrue(uploaded.exists());
		Assert.assertEquals(file.length(), uploaded.length());
		
		InputStream is = download(ua);
		byte[] buff = new byte[(int) (file.length() * 2)];
		int read = is.read(buff);
		is.close();
		Assert.assertEquals(file.length(), read);
	}

	private InputStream download(UserAsset ua) {
		InputStream inputStream = getBuilder("/ws/asset/get/" + ua.getUrlPath()).get(InputStream.class);
		return inputStream;
	}

	public void createPoster() {
		String groupId = "com." + getRandomString(3, 6);
		String artifactId = getRandomString(4, 8);
		int version = 1;
		PosterId posterId = new PosterId(groupId, artifactId, version);
		poster = new Poster(user.getId(), posterId);
		String path = "/ws/poster/create";
		getBuilder(path).post(poster);
		String realPath = poster.getOwner().getRealPath(HOME);
		File file = new File(realPath);
		Assert.assertTrue(file.exists());
	}

	protected void upload(File file, PosterId posterId) {
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		formDataMultiPart.bodyPart(fileDataBodyPart);
//		formDataMultiPart.bodyPart(new FormDataBodyPart("json", json));

		Builder builder = getBuilder("/ws/asset/upload/" + posterId.getUrlPath());
		builder.type(MediaType.MULTIPART_FORM_DATA)
				.accept("application/json;charset=utf-8")
				.post(formDataMultiPart);
	}
}
