package com.gaoshin.amazon.aws;

import com.gaoshin.amazon.jax.SimilarityLookup;
import com.gaoshin.amazon.jax.SimilarityLookupRequest;

public class AWSSimilarityLookup extends AWS {
	private SimilarityLookupRequest itemRequest;
	private SimilarityLookup itemElement;

	public AWSSimilarityLookup() {
        itemRequest = new com.gaoshin.amazon.jax.SimilarityLookupRequest();
		for(String resp : getResponseGroups()) {
			itemRequest.getResponseGroup().add(resp);
		}

        itemElement = new com.gaoshin.amazon.jax.SimilarityLookup();
		itemElement.setAWSAccessKeyId(getAppid());
		itemElement.getRequest().add(itemRequest);
	}
	
	public void addItemId(String itemId) {
		itemRequest.getItemId().add(itemId);
	}
	
	public void setItemIdList(String itemIdList) {
		String[] idList = itemIdList.split("[ ,\r\n\t]");
		for (int i = 0; i < idList.length; i++) {
			if(idList[i] == null || idList[i].length() == 0)
				continue;
			addItemId(idList[i]);
		}
	}
	
	public String[] getResponseGroups() {
		return new String[] {
				"Accessories",
				"BrowseNodes",
				"EditorialReview",
				"Images",
				"Large",
				"ItemAttributes",
				"ItemIds",
				"Medium",
				"Offers",
				"OfferSummary",
				"PromotionSummary",
				"Reviews",
				"SalesRank",
				"Similarities",
				"Tracks",
				"Variations",
				"VariationSummary",
		};
	}

    public String getRequestUrl() throws Exception {
        return getRequestUrl(itemRequest);
    }

    public static void main(String[] args) throws Exception {
        AWSSimilarityLookup lookup = new AWSSimilarityLookup();
        lookup.setItemIdList("B0039JBXVY,0153467916");
        System.out.println(lookup.getRequestUrl());
    }
}
