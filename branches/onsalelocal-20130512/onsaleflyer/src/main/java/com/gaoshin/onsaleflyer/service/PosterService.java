package com.gaoshin.onsaleflyer.service;

import java.io.IOException;

import com.gaoshin.onsaleflyer.beans.Poster;
import com.gaoshin.onsaleflyer.beans.PosterList;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.entity.Visibility;

public interface PosterService {

	void create(String userId, Poster poster) throws IOException;

	void deletePoster(String userId, PosterOwner posterOwner);

	PosterList list(String userId, String ownerId) throws IOException;

	Poster getPoster(String userId, PosterOwner posterOwner) throws IOException;

	void setVisibility(String userId, PosterOwner posterOwner,
			Visibility visibility) throws IOException;

	void save(String userId, Poster poster) throws IOException;
}
