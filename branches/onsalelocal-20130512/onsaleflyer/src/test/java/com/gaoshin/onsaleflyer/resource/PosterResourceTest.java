package com.gaoshin.onsaleflyer.resource;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.gaoshin.onsaleflyer.beans.Poster;
import com.gaoshin.onsaleflyer.beans.PosterId;
import com.gaoshin.onsaleflyer.beans.PosterList;
import com.gaoshin.onsaleflyer.entity.Visibility;

public class PosterResourceTest extends LoginUserResourceTester {
	public static final String HOME = "./target/osf-home";
	
	private Poster posterOwner1;
	private Poster posterOwner2;

	@Test
	public void testAll() {
		testCreate1();
		testCreate2();
		testSave();
		testVisibilityApi();
		testListPosters();
	}
	
	private void testVisibilityApi() {
		String path = "/ws/poster/set-visibility/" + posterOwner1.getOwner().getUrlPath() + "/" + Visibility.Public;
		getBuilder(path).post(" ");
	}

	public void testCreate1() {
		String groupId = "com." + getRandomString(3, 6);
		String artifactId = getRandomString(4, 8);
		int version = 1;
		PosterId posterId = new PosterId(groupId, artifactId, version);
		posterOwner1 = new Poster(user.getId(), posterId);
		String path = "/ws/poster/create";
		getBuilder(path).post(posterOwner1);
		String realPath = posterOwner1.getOwner().getRealPath(HOME);
		File file = new File(realPath);
		Assert.assertTrue(file.exists());
	}

	public void testCreate2() {
		String groupId = posterOwner1.getOwner().getPosterId().getGroupId() + "." + getRandomString(5, 8);
		String artifactId = getRandomString(4, 8);
		int version = 1;
		PosterId posterId = new PosterId(groupId, artifactId, version);
		posterOwner2 = new Poster(user.getId(), posterId);
		String path = "/ws/poster/create";
		getBuilder(path).post(posterOwner2);
	}

	public void testSave() {
		String path = "/ws/poster/save";
		String title = getRandomString(100, 200);
		posterOwner1.setTitle(title);
		getBuilder(path).post(posterOwner1);
		String realPath = posterOwner1.getOwner().getRealPath(HOME);
		Assert.assertTrue(new File(realPath).exists());
	}

	public void testListPosters() {
		String path = "/ws/poster/list";
		PosterList list = getBuilder(path).get(PosterList.class);
		Assert.assertEquals(2, list.getItems().size());
		
		int pubs = 0;
		for(Poster p : list.getItems()) {
			if(p.getVisibility().equals(Visibility.Public) && p.getOwner().getPosterId().equals(posterOwner1.getOwner().getPosterId()))
				pubs++;
		}
		Assert.assertEquals(1, pubs);
		
		logout();
		list = getBuilder(path + "/" + user.getId()).get(PosterList.class);
		Assert.assertEquals(1, list.getItems().size());
	}
}
