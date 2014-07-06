package common.amazon.aws;

import common.amazon.jax.ItemSearchRequest;

public class AWSItemSearch extends AWS {
    
	private ItemSearchRequest itemRequest;

	public AWSItemSearch() {
        itemRequest = new common.amazon.jax.ItemSearchRequest();
		for(String resp : getResponseGroups()) {
			itemRequest.getResponseGroup().add(resp);
		}
	}
	
    public String getRequestUrl() throws Exception {
        return getRequestUrl(itemRequest);
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
                // "RelatedItems",
                "SearchBins",
                "Similarities",
                "Tracks",
                "Variations",
                "VariationSummary", };
    }

}
