package common.amazon.aws;

import java.util.ArrayList;

import common.amazon.jax.BrowseNodeLookupRequest;


public class AWSBrowseNodeLookup extends AWS {

	private BrowseNodeLookupRequest itemRequest;

    public AWSBrowseNodeLookup(String[] itemIdList) {
        init();
        addBrowseNodeIdList(itemIdList);
    }

    public AWSBrowseNodeLookup(String itemId) {
        init();
        addBrowseNodeIdList(itemId);
    }

	public AWSBrowseNodeLookup(ArrayList<String> itemIdList) {
		init();
		addBrowseNodeIdList(itemIdList);
	}

	public void init() {
        itemRequest = new common.amazon.jax.BrowseNodeLookupRequest();

		for(String resp : getResponseGroups()) {
			itemRequest.getResponseGroup().add(resp);
		}
	}

    public void addBrowseNodeIdList(String itemId) {
        itemRequest.getBrowseNodeId().add(itemId);
    }

    public void addBrowseNodeIdList(String[] itemIdList) {
        for(String itemId : itemIdList) {
            itemRequest.getBrowseNodeId().add(itemId);
        }
    }

	public void addBrowseNodeIdList(ArrayList<String> itemIdList) {
		for(String itemId : itemIdList) {
			itemRequest.getBrowseNodeId().add(itemId);
		}
	}

	public String getBrowseNodeIdList() {
		StringBuffer sb = new StringBuffer();
		for(String id : itemRequest.getBrowseNodeId()) {
			sb.append(id).append(' ');
		}
		return sb.toString();
	}

	public String getItemIdList() {
		StringBuffer sb = new StringBuffer();
		for(String id : itemRequest.getBrowseNodeId()) {
			sb.append(id).append(' ');
		}
		return sb.toString();
	}

    public String getRequestUrl() throws Exception {
        return getRequestUrl(itemRequest);
    }

	public String[] getResponseGroups() {
		return new String[] {
                // "MostGifted",
                // "NewReleases",
                // "MostWishedFor",
                "TopSellers",
                "BrowseNodeInfo",
		};
	}

    public static void main(String[] args) throws Exception {
        String[] itemIdList = { "22999534", "229534", "491286" };
        AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(itemIdList);
    }
}
