package com.gaoshin.amazon.aws;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gaoshin.amazon.jax.Item;
import com.gaoshin.amazon.jax.ItemLookupRequest;
import com.gaoshin.amazon.jax.ItemLookupResponse;
import com.gaoshin.amazon.jax.Items;


public class AWSItemLookup extends AWS {
	private static final Log log = LogFactory.getLog(AWSItemLookup.class);

	private static JAXBContext jaxbContext = null;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(ItemLookupResponse.class);
		} catch (JAXBException e) {
		}
	}

	private ItemLookupRequest itemRequest;
	private ItemLookupResponse response;

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
		itemRequest = new com.gaoshin.amazon.jax.ItemLookupRequest();

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

	public ItemLookupResponse process() throws InvalidKeyException, NoSuchAlgorithmException, IOException, JAXBException {
		String requestUrl = getRequestUrl(itemRequest);
		URL url = new URL(requestUrl);
		sync();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		response = (ItemLookupResponse) unmarshaller.unmarshal(conn.getInputStream());
		return response;
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

	public ItemLookupResponse getResponse() {
		return response;
	}

	public Item getItem(String asin) {
		for(Items items : response.getItems()) {
			for(Item item : items.getItem()) {
				if(asin.equals(item.getASIN())) {
                    return item;
                }
			}
		}
		return null;
	}

	public boolean hasItemInResponse(String asin) {
		return getItem(asin) != null;
	}

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException, JAXBException {
        String[] itemIdList = { "B0039JBXVY", "0153467916" };
		AWSItemLookup lookup = new AWSItemLookup(itemIdList);
		lookup.process();
		ItemLookupResponse resp = lookup.getResponse();
		System.out.println(resp.getOperationRequest().getRequestId());
		System.out.println(resp.getItems().get(0).getItem().get(0).getDetailPageURL());
	}
}
