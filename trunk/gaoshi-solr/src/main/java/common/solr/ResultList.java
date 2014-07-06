package common.solr;

import java.util.ArrayList;

public class ResultList<T> extends ArrayList<T> {
	private long start = 0;
	private long total = 0;
	
	public ResultList() {
	}
	
	public ResultList(long total) {
		this.setTotal(total);
	}
	
	public ResultList(long total, long start) {
		this.setTotal(total);
		this.setStart(start);
	}

	public long getStart() {
		return start;
	}

	public long getTotal() {
		if(total == 0)
			total = size();
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setStart(long start) {
		this.start = start;
	}
	
}
