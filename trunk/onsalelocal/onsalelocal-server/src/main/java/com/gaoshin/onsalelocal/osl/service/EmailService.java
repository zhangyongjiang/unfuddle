package com.gaoshin.onsalelocal.osl.service;

import java.util.List;

import com.nextshopper.osl.entity.EmailOffer;

public interface EmailService {
	void process(boolean delete, int threshold);
	List<EmailOffer> list(int offset, int size);
	EmailOffer getEmailOfferDetails(String id);
}
