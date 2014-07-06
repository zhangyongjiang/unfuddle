package com.gaoshin.onsalelocal.slocal.service;

import com.gaoshin.onsalelocal.api.OfferDetailsList;

public interface SlocalService {

	void updateTasks();

	void seedCityTasks();

	OfferDetailsList searchOffer(float lat, float lng, int radius, int offset, int size);

	void geo();

	int getPendingCityCategoryTaskCount();

	void ondemand(String cityId);

}
