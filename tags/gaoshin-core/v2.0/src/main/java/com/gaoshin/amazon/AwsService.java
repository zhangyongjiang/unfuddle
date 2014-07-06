package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.List;

import com.gaoshin.beans.User;

public interface AwsService {
    AwsBrowseNodeList getUnloadedBrowseNodes(int size);

    void addNodes(List<AwsBrowseNode> tbd);

    void setBadNode(String id);

    void setBadNodes(List<String> idList);

    AwsBrowseNodeList getTopBrowseNodes();

    AwsBrowseNode getBrowseNode(String nodeId);

    AwsBrowseNodeList getUnitemedBrowseNodes(int size);

    void setNodeItemed(String id);

    void setNodeItemed(List<String> idList);

    void setNodeListItemed(List<AwsBrowseNode> idList);

    void addUnloadedNodes(List<AwsBrowseNode> tbd);

    void addUnloadedItems(List<AwsItem> tbd);

    AwsItemList getUnloadedItems(int i);

    void setBadItems(ArrayList<String> nodeIdList);

    void setItemsLoaded(ArrayList<String> nodeIdList);

    AwsBrowseNodeList search(String keywords);

    AwsItem getItem(String asin);

    AwsItem addInterest(User user, ItemInterest interest);

    AwsItem removeInterest(User user, Long interestId);

    AwsItem getItem(String asin, float latitude, float longitude, int miles);

    AwsBrowseNode addNodeInterest(NodeInterest nodeInterest);

    NodeInterestList getNodeInterest(User user, String nodeId);
}
