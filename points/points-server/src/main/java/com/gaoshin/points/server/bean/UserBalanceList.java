package com.gaoshin.points.server.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserBalanceList {
	private List<UserBalance> list = new ArrayList<UserBalance>();

	public List<UserBalance> getList() {
		return list;
	}

	public void setList(List<UserBalance> list) {
		this.list = list;
	}
	
	public UserBalance findItem(String itemId) {
		for(UserBalance ub : list) {
			if(ub.getItemId().equals(itemId)) {
				return ub;
			}
		}
		return null;
	}
}
