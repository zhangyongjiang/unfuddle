package common.amazon.aws;

import common.amazon.jax.SimilarityLookupRequest;

public class AWSSimilarityLookup extends AWS {
	private SimilarityLookupRequest itemRequest;

	public AWSSimilarityLookup() {
        itemRequest = new common.amazon.jax.SimilarityLookupRequest();
		for(String resp : getResponseGroups()) {
			itemRequest.getResponseGroup().add(resp);
		}
	}
	
    public String getRequestUrl() throws Exception {
        return getRequestUrl(itemRequest);
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
                "VariationSummary", };
    }
}
