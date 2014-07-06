package com.gaoshin.amazon.aws;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.gaoshin.amazon.jax.ItemSearch;
import com.gaoshin.amazon.jax.ItemSearchRequest;
import com.gaoshin.amazon.jax.ItemSearchResponse;
import com.gaoshin.amazon.jax.Items;

public class AWSItemSearch extends AWS {
	private static JAXBContext jaxbContext = null;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(ItemSearchResponse.class);
		} catch (JAXBException e) {
		}
	}
    
	private ItemSearchRequest itemRequest;
	private ItemSearch itemElement;
	private ItemSearchResponse resp;

	public AWSItemSearch() {
        itemRequest = new com.gaoshin.amazon.jax.ItemSearchRequest();
		for(String resp : getResponseGroups()) {
			itemRequest.getResponseGroup().add(resp);
		}

        itemElement = new com.gaoshin.amazon.jax.ItemSearch();
		itemElement.setAWSAccessKeyId(getAppid());
		itemElement.getRequest().add(itemRequest);
	}
	
	public ItemSearchResponse process() {
		try {
			URL url = new URL(getRequestUrl(itemRequest));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			resp = (ItemSearchResponse)unmarshaller.unmarshal(conn.getInputStream());
			return resp;
		} catch (Exception e) {
			return null;
		}
		
//		logger.info("getServicePort");
//		AWSECommerceServicePortType port = getServicePort();
//		logger.info("itemSearch");
//		ItemSearchResponse resp = port.itemSearch(itemElement);
//		logger.info("Search done");
//		return resp;
	}

	public void setSearchIndex(String index) {
		itemRequest.setSearchIndex(index);
	}
	
	public void setCondition(String condition) {
		itemRequest.setCondition(condition);
	}

	public void setKeywords(String kw) {
		itemRequest.setKeywords(kw);
	}
	
	public String[] getResponseGroups() {
		return new String[] {
				"Accessories",
				"BrowseNodes",
				"EditorialReview",
				"ItemAttributes",
				"ItemIds",
				"Large",
				"Medium",
				"OfferFull",
				"Offers",
				"OfferSummary",
				"Reviews",
				//"RelatedItems",
				"SearchBins",
				"Similarities",
				"Tracks",
				"Variations",
				"VariationSummary",
		};
	}
	
	public List<Items> getItemsList() {
		return resp.getItems();
	}

    public String getRequestUrl() throws Exception {
        return getRequestUrl(itemRequest);
    }

    public static void main(String[] args) throws Exception {
        AWSItemSearch lookup = new AWSItemSearch();
        lookup.setKeywords("samsung hdtv");
        lookup.setSearchIndex(AwsSearchIndex.Electronics.name());
        System.out.println(lookup.getRequestUrl());
    }
}
