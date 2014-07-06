package com.gaoshin.points.server.service;

import com.gaoshin.points.server.bean.Cashier;
import com.gaoshin.points.server.bean.ExchangeHistory;
import com.gaoshin.points.server.bean.Item;
import com.gaoshin.points.server.bean.ItemList;

public interface MerchantService {

	void setAsMerchant(String userId);

	Item createItem(String userId, Item item);

	Item getItemById(String itemId);

	ItemList listVarieties(String merchantId);

	ExchangeHistory adjustPoints(String merchantId, ExchangeHistory trade);

	ExchangeHistory adjustPointsByPhone(String merchantId, String phone,
			ExchangeHistory trade);

	Cashier addCashier(String userId, Cashier cashier);

	Cashier cashierLogin(Cashier cashier);

}
