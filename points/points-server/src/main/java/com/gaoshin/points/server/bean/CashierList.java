package com.gaoshin.points.server.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CashierList {
	private List<Cashier> list = new ArrayList<Cashier>();

	public List<Cashier> getList() {
		return list;
	}

	public void setList(List<Cashier> list) {
		this.list = list;
	}
}
