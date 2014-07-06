package com.gaoshin.amazon.aws;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.gaoshin.amazon.jax.BrowseNodeLookupResponse;
import com.gaoshin.amazon.jax.ItemLookupResponse;


@Component
public class AmazonComponent {

    public ItemLookupResponse lookupItems(String itemId) throws Exception {
        AWSItemLookup lookup = new AWSItemLookup(itemId);
        return lookup.process();
    }

    public ItemLookupResponse lookupItems(String[] itemIds) throws Exception {
        AWSItemLookup lookup = new AWSItemLookup(itemIds);
        return lookup.process();
    }

    public ItemLookupResponse lookupItems(ArrayList<String> itemIds) throws Exception {
        AWSItemLookup lookup = new AWSItemLookup(itemIds);
        return lookup.process();
    }

    public BrowseNodeLookupResponse lookupBrowseNodes(String browseNodeId) throws Exception {
        AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(browseNodeId);
        lookup.process();
        return lookup.getResponse();
    }

    public BrowseNodeLookupResponse lookupBrowseNodes(String[] browseNodeIds) throws Exception {
        AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(browseNodeIds);
        lookup.process();
        return lookup.getResponse();
    }

    public BrowseNodeLookupResponse lookupBrowseNodes(ArrayList<String> browseNodeIds) throws Exception {
        AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(browseNodeIds);
        lookup.process();
        return lookup.getResponse();
    }
}
