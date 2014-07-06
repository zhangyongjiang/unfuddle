package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.gaoshin.amazon.aws.AWSBrowseNodeLookup;
import com.gaoshin.amazon.aws.AWSItemLookup;
import com.gaoshin.amazon.jax.BrowseNode;
import com.gaoshin.amazon.jax.BrowseNodeLookupResponse;
import com.gaoshin.amazon.jax.BrowseNodes;
import com.gaoshin.amazon.jax.Item;
import com.gaoshin.amazon.jax.ItemLookupResponse;
import com.gaoshin.amazon.jax.Items;
import com.gaoshin.amazon.jax.TopItemSet;
import com.gaoshin.amazon.jax.TopItemSet.TopItem;
import com.gaoshin.amazon.jax.TopSellers.TopSeller;
import com.gaoshin.webservice.GaoshinResource;
import com.sun.jersey.spi.inject.Inject;

@Path("/aws")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class AwsResource extends GaoshinResource {
    private AwsService awsService;
    private boolean loop = true;

    @Inject
    public void setAwsService(AwsService awsService) {
        this.awsService = awsService;

        List<AwsBrowseNode> seeds = new ArrayList<AwsBrowseNode>();
        String nodeId = "1266092011";
        AwsBrowseNode node = awsService.getBrowseNode(nodeId);
        if (node == null) {
            node = new AwsBrowseNode();
            node.setId(nodeId);
            seeds.add(node);
            awsService.addUnloadedNodes(seeds);
        }

        Thread thread = new Thread(new NodeLoader());
        thread.start();

        Thread nodeItemThread = new Thread(new NodeItemLoader());
        nodeItemThread.start();

        Thread itemThread = new Thread(new ItemLoader());
        itemThread.start();
    }

    @GET
    @Path("tops")
    public AwsBrowseNodeList getTops() {
        return awsService.getTopBrowseNodes();
    }

    @GET
    @Path("search")
    public AwsBrowseNodeList search(@QueryParam("q") String keywords) {
        return awsService.search(keywords);
    }

    @GET
    @Path("node/{nodeId}")
    public AwsBrowseNode getNode(@PathParam("nodeId") String nodeId) {
        return awsService.getBrowseNode(nodeId);
    }

    @GET
    @Path("xnode/{nodeId}")
    public Response getNodeInXml(@PathParam("nodeId") String nodeId) {
        AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(nodeId);
        lookup.process();
        BrowseNodeLookupResponse resp = lookup.getResponse();
        return Response.ok(resp, MediaType.TEXT_XML_TYPE).build();
    }

    @GET
    @Path("xitem/{itemId}")
    public Response getItemInXml(@PathParam("itemId") String itemId) throws Exception {
        AWSItemLookup lookup = new AWSItemLookup(itemId);
        lookup.process();
        ItemLookupResponse resp = lookup.getResponse();
        return Response.ok(resp, MediaType.TEXT_XML_TYPE).build();
    }

    private class NodeLoader implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    AwsBrowseNodeList list = AwsResource.this.awsService.getUnloadedBrowseNodes(10);
                    if (list.getList().size() == 0) {
                        Thread.sleep(1000);
                        continue;
                    }

                    ArrayList<String> nodeIdList = new ArrayList<String>();
                    for (AwsBrowseNode node : list.getList()) {
                        nodeIdList.add(node.getId());
                    }
                    AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(nodeIdList);
                    lookup.process();
                    BrowseNodeLookupResponse resp = lookup.getResponse();

                    List<AwsBrowseNode> tbd = new ArrayList<AwsBrowseNode>();
                    for (BrowseNodes nodes : resp.getBrowseNodes()) {
                        for (BrowseNode node : nodes.getBrowseNode()) {
                            nodeIdList.remove(node.getBrowseNodeId());

                            AwsBrowseNode awsNode = new AwsBrowseNode();
                            awsNode.setId(node.getBrowseNodeId());
                            awsNode.setName(node.getName());
                            awsNode.setRoot(node.isIsCategoryRoot() == null ? false : node.isIsCategoryRoot());

                            if (node.getChildren() != null) {
                                for (BrowseNode child : node.getChildren().getBrowseNode()) {
                                    awsNode.getChildren().add(new AwsBrowseNode(child.getBrowseNodeId()));
                                }
                            }

                            if (node.getAncestors() != null) {
                                for (BrowseNode ancestor : node.getAncestors().getBrowseNode()) {
                                    awsNode.getParents().add(new AwsBrowseNode(ancestor.getBrowseNodeId()));
                                }
                            }

                            tbd.add(awsNode);

                            if (node.getTopSellers() != null) {
                                for (TopSeller ts : node.getTopSellers().getTopSeller()) {
                                    AwsItem topSeller = new AwsItem();
                                    topSeller.setAsin(ts.getASIN());
                                    awsNode.getTopSellers().add(topSeller);
                                }
                            }
                        }
                    }
                    AwsResource.this.awsService.addNodes(tbd);
                    AwsResource.this.awsService.setBadNodes(nodeIdList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (loop);
        }
    }

    private class NodeItemLoader implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    dojob();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (loop);
        }

        public void dojob() throws Exception {
            AwsBrowseNodeList list = AwsResource.this.awsService.getUnitemedBrowseNodes(10);
            if (list.getList().size() == 0) {
                Thread.sleep(1000);
                return;
            }

            ArrayList<String> nodeIdList = new ArrayList<String>();
            for (AwsBrowseNode node : list.getList()) {
                nodeIdList.add(node.getId());
            }
            AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(nodeIdList);
            lookup.process();
            BrowseNodeLookupResponse resp = lookup.getResponse();

            List<AwsItem> tbd = new ArrayList<AwsItem>();
            for (BrowseNodes nodes : resp.getBrowseNodes()) {
                for (BrowseNode node : nodes.getBrowseNode()) {
                    if (node.getTopSellers() != null) {
                        for (TopSeller ts : node.getTopSellers().getTopSeller()) {
                            String asin = ts.getASIN();
                            AwsItem item = new AwsItem();
                            item.setAsin(asin);
                            item.setTitle(ts.getTitle());
                            tbd.add(item);
                        }
                    }
                    if (node.getTopItemSet() != null) {
                        for (TopItemSet tis : node.getTopItemSet()) {
                            for (TopItem ts : tis.getTopItem()) {
                                String asin = ts.getASIN();
                                AwsItem item = new AwsItem();
                                item.setAsin(asin);
                                item.setTitle(ts.getTitle());
                                tbd.add(item);
                            }
                        }
                    }
                }
            }
            AwsResource.this.awsService.addUnloadedItems(tbd);
            awsService.setNodeListItemed(list.getList());
        }
    }

    private class ItemLoader implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    dojob();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (loop);
        }

        public void dojob() throws Exception {
            AwsItemList list = AwsResource.this.awsService.getUnloadedItems(10);
            if (list.getList().size() == 0) {
                Thread.sleep(1000);
                return;
            }

            ArrayList<String> nodeIdList = new ArrayList<String>();
            for (AwsItem node : list.getList()) {
                nodeIdList.add(node.getAsin());
            }
            AWSItemLookup lookup = new AWSItemLookup(nodeIdList);
            lookup.process();
            ItemLookupResponse resp = lookup.getResponse();

            HashMap<String, AwsBrowseNode> bag = new HashMap<String, AwsBrowseNode>();
            ArrayList<String> loadedIdList = new ArrayList<String>();
            for (Items items : resp.getItems()) {
                for (Item item : items.getItem()) {
                    nodeIdList.remove(item.getASIN());
                    loadedIdList.add(item.getASIN());
                    if (item.getBrowseNodes() != null) {
                        for (BrowseNode bn : item.getBrowseNodes().getBrowseNode()) {
                            collectBrowseNode(bn, bag);
                        }
                    }
                }
            }

            List<AwsBrowseNode> tbd = new ArrayList<AwsBrowseNode>();
            tbd.addAll(bag.values());

            AwsResource.this.awsService.addUnloadedNodes(tbd);
            AwsResource.this.awsService.setBadItems(nodeIdList);
            AwsResource.this.awsService.setItemsLoaded(loadedIdList);
        }

        private void collectBrowseNode(BrowseNode bn, HashMap<String, AwsBrowseNode> bag) {
            if (bag.containsKey(bn.getBrowseNodeId())) {
                return;
            }
            AwsBrowseNode abn = new AwsBrowseNode();
            abn.setId(bn.getBrowseNodeId());
            abn.setName(bn.getName());
            abn.setLoaded(false);
            bag.put(bn.getBrowseNodeId(), abn);

            if (bn.getChildren() != null) {
                for (BrowseNode child : bn.getChildren().getBrowseNode()) {
                    collectBrowseNode(child, bag);
                }
            }

            if (bn.getAncestors() != null) {
                for (BrowseNode ancestor : bn.getAncestors().getBrowseNode()) {
                    collectBrowseNode(ancestor, bag);
                }
            }
        }
    }
}
