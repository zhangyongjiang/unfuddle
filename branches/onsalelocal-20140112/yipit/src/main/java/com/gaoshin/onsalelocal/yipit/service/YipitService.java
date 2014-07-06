package com.gaoshin.onsalelocal.yipit.service;

import com.gaoshin.onsalelocal.api.OfferDetailsList;

public interface YipitService {

	void updateTasks();

	void seedTasks();

	void seedDivisions() throws Exception;

	void seedDivisionTasks();

	void seedSources() throws Exception;

	void seedCityTasks();

	OfferDetailsList searchOffer(float lat, float lng, int radius);

}
