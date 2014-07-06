package com.gaoshin.onsalelocal.osl.beans;

import java.util.ArrayList;
import java.util.List;

import com.gaoshin.onsalelocal.api.OfferDetails;

public class OfferSearchResponse {
	private long offset;
	private long size;
	private long total;
	private List<OfferDetails> items = new ArrayList<OfferDetails>();

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<OfferDetails> getItems() {
		return items;
	}

	public void setItems(List<OfferDetails> items) {
		this.items = items;
	}

}
