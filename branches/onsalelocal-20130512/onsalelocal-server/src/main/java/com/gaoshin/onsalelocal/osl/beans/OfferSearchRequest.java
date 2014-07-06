package com.gaoshin.onsalelocal.osl.beans;

public class OfferSearchRequest {
	private String category;
	private String subcategory;
	private String keywords;
	private Float lat;
	private Float lng;
	private float radius;
	private int offset;
	private int size;
	private String merchant;
	private SearchOrder order;
	private String source;
	private String group;
	private int groupLimit = 1;
	private String exclude;
	private String nostore;
	private Boolean localService;

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = format(keywords);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = format(merchant);
	}

	public static String format(String s) {
		if(s == null)
			return s;
		s = s.trim();
		if(s.length() == 0)
			return null;
		return s;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public SearchOrder getOrder() {
		return order;
	}

	public void setOrder(SearchOrder order) {
		this.order = order;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = format(source);
	}

	public String getCategory() {
	    return category;
    }

	public void setCategory(String category) {
	    this.category = category;
    }

	public String getGroup() {
	    return group;
    }

	public void setGroup(String group) {
	    this.group = group;
    }

	public int getGroupLimit() {
	    return groupLimit;
    }

	public void setGroupLimit(int groupLimit) {
	    this.groupLimit = groupLimit;
    }

	public String getExclude() {
	    return exclude;
    }

	public void setExclude(String exclude) {
	    this.exclude = exclude;
    }

	public String getNostore() {
	    return nostore;
    }

	public void setNostore(String nostore) {
	    this.nostore = nostore;
    }

	public String getSubcategory() {
	    return subcategory;
    }

	public void setSubcategory(String subcategory) {
	    this.subcategory = subcategory;
    }

	public Boolean getLocalService() {
	    return localService;
    }

	public void setLocalService(Boolean localService) {
	    this.localService = localService;
    }
}
