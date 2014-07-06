
package com.gaoshin.amazon.jax;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "url",
    "price",
    "createdBy",
    "createdOn",
    "customerReviews"
})
@XmlRootElement(name = "ItemOffer")
public class ItemOffer {

    @XmlElement(name = "URL")
    protected String url;

    @XmlElement(name = "Price")
    protected BigDecimal price;
    
    @XmlElement(name = "CreatedBy")
    protected String createdBy;
    
    @XmlElement(name = "CreatedOn")
    protected Date createdOn = new Date();
    
    @XmlElement(name = "CustomerReviews")
    protected CustomerReviews customerReviews;
    
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public CustomerReviews getCustomerReviews() {
		return customerReviews;
	}
	
	public void setCustomerReviews(CustomerReviews customerReviews) {
		this.customerReviews = customerReviews;
	}

	public String getSite() {
		if(url == null)
			return null;
		
		try {
			URL u = new URL(url);
			String host = u.getHost();
			if(host.startsWith("www."))
				host = host.substring(4);
			return host;
		} catch (MalformedURLException e) {
		}
		
		return null;
	}

	public static String getSite(String url) {
		if(url == null)
			return null;
		
		try {
			URL u = new URL(url);
			String host = u.getHost();
			if(host.startsWith("www."))
				host = host.substring(4);
			return host;
		} catch (MalformedURLException e) {
		}
		
		return null;
	}
	
	public boolean isSameSite(String siteOrUrl) {
    	if(siteOrUrl.startsWith("http"))
    		siteOrUrl = ItemOffer.getSite(siteOrUrl);
		return siteOrUrl.equalsIgnoreCase(getSite());
	}
}
