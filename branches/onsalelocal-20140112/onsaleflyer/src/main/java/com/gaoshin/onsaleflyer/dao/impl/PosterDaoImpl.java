package com.gaoshin.onsaleflyer.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsaleflyer.beans.Poster;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.beans.UserAsset;
import com.gaoshin.onsaleflyer.dao.PosterDao;
import com.gaoshin.onsaleflyer.entity.Flyer;
import com.gaoshin.onsaleflyer.entity.Offer;
import com.gaoshin.onsaleflyer.entity.PosterItem;
import com.gaoshin.onsaleflyer.entity.Visibility;
import common.db.dao.impl.GenericDaoImpl;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Repository
public class PosterDaoImpl extends GenericDaoImpl implements PosterDao {
	private static final Logger log = Logger.getLogger(PosterDaoImpl.class);

	private static final String FileNameFriendList = ".friends";
	
	@Value("${asset.base}")
	private String base;
	
	@Override
	public List<Flyer> listFlyersOfPoster(String posterId) {
		return query(Flyer.class, Collections.singletonMap("posterId", posterId));
	}

	@Override
	public List<Offer> listOffersOfPoster(String posterId) {
		return query(Offer.class, Collections.singletonMap("posterId", posterId));
	}

	@Override
	public List<PosterItem> listPosterItems(String posterId) {
		return query(PosterItem.class, Collections.singletonMap("posterId", posterId));
	}
	
	@Override
	public void create(Poster poster) throws IOException {
		String realPath = poster.getOwner().getRealPath(base);
		File file = new File(realPath);
		if(file.exists())
			throw new BusinessException(ServiceError.Duplicated);
		file.mkdirs();
		save(poster);
	}

	@Override
	public void deletePoster(PosterOwner posterOwner) {
		String realPath = posterOwner.getRealPath(base);
		File file = new File(realPath);
		if(!file.exists()) {
			throw new BusinessException(ServiceError.NotFound);
		}
		File[] children = file.listFiles();
		for(File f : children) {
			if(f.isFile()) {
				f.delete();
			}
		}
	}

	@Override
	public void save(Poster poster) throws IOException {
		String realPath = poster.getOwner().getRealPath(base);
		File file = new File(realPath);
		if(!file.exists()) {
			throw new BusinessException(ServiceError.NotFound);
		}
		UserAsset ua = poster.getOwner().getContentAsset();
		String contentPath = ua.getRealPath(base);
		FileWriter fw = new FileWriter(contentPath);
		ObjectMapper om = new ObjectMapper();
		om.writeValue(fw, poster);
		fw.close();
		
		setVisibility(poster.getOwner(), poster.getVisibility());
	}

	@Override
	public List<Poster> list(String userId, String ownerId) throws IOException {
		String path = base + File.separator + PosterOwner.getPath(ownerId);
		return listPostersInDir(userId, ownerId, path);
	}

	private List<Poster> listPostersInDir(String userId, String ownerId, String path) throws IOException {
		List<Poster> posters = new ArrayList<Poster>();
		File contentFile = new File(path + File.separator + PosterOwner.ContentFileName);
		if(contentFile.exists()) {
			UserAsset ua = UserAsset.fromPath(ownerId, contentFile.getAbsolutePath());
			if(isVisibleTo(ua.getPosterOwner(), userId)) {
				FileInputStream fis = new FileInputStream(contentFile);
				ObjectMapper om = new ObjectMapper();
				Poster poster = om.readValue(fis, Poster.class);
				fis.close();
				
				for(Visibility v : Visibility.class.getEnumConstants()) {
					File file = new File(path + File.separator + v.getFileName());
					if(file.exists())
						poster.setVisibility(v);
				}
				
				poster.getOwner().setPosterId(ua.getPosterOwner().getPosterId());
				poster.getOwner().setOwnerId(ownerId);
				posters.add(poster);
			}
		}
		
		File dir = new File(path);
		for(File child : dir.listFiles()) {
			if(child.isDirectory()) {
				posters.addAll(listPostersInDir(userId, ownerId, child.getAbsolutePath()));
			}
		}
 		
		return posters;
	}

	@Override
	public Poster getPoster(PosterOwner posterOwner) throws IOException {
		UserAsset ua = posterOwner.getContentAsset();
		FileInputStream fis = new FileInputStream(ua.getRealPath(base));
		ObjectMapper om = new ObjectMapper();
		Poster poster = om.readValue(fis, Poster.class);
		fis.close();
		
		poster.setVisibility(getVisibility(posterOwner));
		
		return poster;
	}
	
	@Override
	public Visibility getVisibility(PosterOwner up) {
		{
			UserAsset asset = up.getVisibilityAsset(Visibility.Public);
			File file = new File(asset.getRealPath(base));
			if(file.exists())
				return Visibility.Public;
		}
		{
			UserAsset asset = up.getVisibilityAsset(Visibility.Friend);
			File file = new File(asset.getRealPath(base));
			if(file.exists())
				return Visibility.Friend;
		}
		return Visibility.Private;
	}

	@Override
	public boolean isVisibleTo(PosterOwner up, String userId) throws IOException {
		if(up.getOwnerId().equals(userId))
			return true;
		Visibility v = getVisibility(up);
		if(Visibility.Public.equals(v))
			return true;
		if(userId == null)
			return false;
		
		List<String> friends = listFriends(up);
		return friends.contains(userId);
	}

	@Override
	public void setVisibility(PosterOwner posterOwner, Visibility visibility)
			throws IOException {
		for(Visibility v : Visibility.class.getEnumConstants()) {
			if(v.equals(visibility)) {
				createVisibilityFile(posterOwner, v);
			}
			else {
				removeVisibilityFile(posterOwner, v);
			}
		}
	}
	
	private List<String> listFriends(PosterOwner up) throws IOException {
		UserAsset ua = new UserAsset(up, FileNameFriendList);
		String path = ua.getRealPath(base);
		File file = new File(path);
		if(!file.exists())
			return Collections.EMPTY_LIST;
		byte[] buff = new byte[(int) file.length()];
		FileInputStream in = new FileInputStream(file);
		in.read(buff);
		in.close();
		String[] friends = new String(buff).split("[\n\r]+");
		return Arrays.asList(friends);
	}
	
	private void removeVisibilityFile(PosterOwner posterOwner, Visibility visibility) {
		UserAsset ua = posterOwner.getVisibilityAsset(visibility);
		File file = new File(ua.getRealPath(base));
		if(file.exists())
			file.delete();
	}
	
	private void createVisibilityFile(PosterOwner posterOwner, Visibility visibility) throws IOException {
		UserAsset ua = posterOwner.getVisibilityAsset(visibility);
		File file = new File(ua.getRealPath(base));
		if(!file.exists())
			file.createNewFile();
	}
}
