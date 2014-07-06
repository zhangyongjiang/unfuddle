package com.gaoshin.amazon.aws;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.gaoshin.amazon.jax.BrowseNode;
import com.gaoshin.amazon.jax.BrowseNodeLookup;
import com.gaoshin.amazon.jax.BrowseNodeLookupRequest;
import com.gaoshin.amazon.jax.BrowseNodeLookupResponse;
import com.gaoshin.amazon.jax.BrowseNodes;


public class AWSBrowseNodeLookup extends AWS {

	private static JAXBContext jaxbContext = null;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(BrowseNodeLookupResponse.class);
		} catch (JAXBException e) {
            e.printStackTrace();
		}
	}

	private BrowseNodeLookupRequest itemRequest;
	private BrowseNodeLookup itemElement;
	private BrowseNodeLookupResponse response;

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
		itemRequest = new com.gaoshin.amazon.jax.BrowseNodeLookupRequest();

		for(String resp : getResponseGroups()) {
			itemRequest.getResponseGroup().add(resp);
		}
		itemElement = new com.gaoshin.amazon.jax.BrowseNodeLookup();
		itemElement.setAWSAccessKeyId(getAppid());
		itemElement.getRequest().add(itemRequest);
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

	public void process() {
		try {
			URL url = new URL(getRequestUrl(itemRequest));
            sync();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			response = (BrowseNodeLookupResponse) unmarshaller.unmarshal(conn.getInputStream());
		} catch (Exception e) {
            e.printStackTrace();
		}
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

	public BrowseNodeLookupResponse getResponse() {
		return response;
	}

	public BrowseNode getBrowseNode(String browseNodeId) {
		List<BrowseNodes> browseNodesList = response.getBrowseNodes();
		if(browseNodesList == null) {
            return null;
        }
		for(BrowseNodes browseNodes : browseNodesList) {
			for(BrowseNode browseNode : browseNodes.getBrowseNode()) {
				if(browseNodeId.equals(browseNode.getBrowseNodeId())) {
                    return browseNode;
                }
			}
		}
		return null;
	}

	public boolean hasBrowseNodeInResponse(String browseNodeId) {
		return getBrowseNode(browseNodeId) != null;
	}

    public static void main(String[] args) throws Exception {
        String[] itemIdList = { "22999534", "229534", "491286" };
        AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(itemIdList);
        lookup.process();
        BrowseNodeLookupResponse resp = lookup.getResponse();
        System.out.println(resp.getOperationRequest().getRequestId());
    }
}
