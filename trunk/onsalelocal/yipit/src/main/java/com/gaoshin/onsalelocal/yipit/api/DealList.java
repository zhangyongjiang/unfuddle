package com.gaoshin.onsalelocal.yipit.api;

import java.util.ArrayList;
import java.util.List;

public class DealList {
	private List<Deal> deals = new ArrayList<Deal>();

	public List<Deal> getDeals() {
		return deals;
	}

	public void setDeals(List<Deal> deals) {
		this.deals = deals;
	}
}
