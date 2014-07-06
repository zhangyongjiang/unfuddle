package common.amazon.aws;

import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import common.amazon.jax.ItemLookupRequest;


public class AWSItemLookup extends AWS {
	private static final Log log = LogFactory.getLog(AWSItemLookup.class);

	private ItemLookupRequest itemRequest;

	public AWSItemLookup(String itemId) {
		init();
		addItemIdList(new String[]{itemId});
	}

	public AWSItemLookup(String[] itemIdList) {
		init();
		addItemIdList(itemIdList);
	}

	public AWSItemLookup(ArrayList<String> itemIdList) {
		init();
		addItemIdList(itemIdList);
	}

	public void init() {
        itemRequest = new common.amazon.jax.ItemLookupRequest();

		for(String resp : getResponseGroups()) {
			itemRequest.getResponseGroup().add(resp);
		}
		itemRequest.setTagsPerPage(new BigInteger("10"));
	}

	public void addItemIdList(String[] itemIdList) {
		for(String itemId : itemIdList) {
			itemRequest.getItemId().add(itemId);
		}
	}

	public void addItemIdList(ArrayList<String> itemIdList) {
		for(String itemId : itemIdList) {
			itemRequest.getItemId().add(itemId);
		}
	}

	public String getItemIdList() {
		StringBuffer sb = new StringBuffer();
		for(String id : itemRequest.getItemId()) {
			sb.append(id).append(' ');
		}
		return sb.toString();
	}

    public String getRequestUrl() throws Exception {
        return getRequestUrl(itemRequest);
    }

	public void setSearchIndex(String index) {
		itemRequest.setSearchIndex(index);
	}

	public String[] getResponseGroups() {
		return new String[] {
				"Small",
				"BrowseNodes",
				"Images",
				"ItemAttributes",
				"ItemIds",
				"Large",
				"Medium",
                // "MerchantItemAttributes",
				"Accessories",
//				"RelatedItems",
                // "ListmaniaLists",
				"EditorialReview",
				"OfferFull",
				"Offers",
                // "PromotionDetails",
                // "PromotionSummary",
				"OfferSummary",
				"Tracks",
				"VariationImages",
                // "VariationMinimum",
                // "Variations",
				"VariationSummary",
				"Reviews",
				"SalesRank",
				"Similarities",
                // "Subjects",
                // "Tags",
        // "TagsSummary",
		};
	}

    public static void main(String[] args) throws Exception {
        String[] itemIdList = { "B0039JBXVY", "0153467916" };
		AWSItemLookup lookup = new AWSItemLookup(itemIdList);
	}
}
