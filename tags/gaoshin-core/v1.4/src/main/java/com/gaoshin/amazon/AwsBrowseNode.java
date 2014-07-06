package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AwsBrowseNode {
    private String id;
    private String name;
    private boolean root;
    private List<AwsBrowseNode> children = new ArrayList<AwsBrowseNode>();
    private List<AwsBrowseNode> parents = new ArrayList<AwsBrowseNode>();
    private boolean loaded = false;
    private boolean badNode = false;
    private int retry = 0;
    private List<NodeInterest> interests = new ArrayList<NodeInterest>();

    public AwsBrowseNode() {
    }

    public AwsBrowseNode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public List<AwsBrowseNode> getChildren() {
        return children;
    }

    public void setChildren(List<AwsBrowseNode> children) {
        this.children = children;
    }

    public List<AwsBrowseNode> getParents() {
        return parents;
    }

    public void setParents(List<AwsBrowseNode> parents) {
        this.parents = parents;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setBadNode(boolean badNode) {
        this.badNode = badNode;
    }

    public boolean isBadNode() {
        return badNode;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getRetry() {
        return retry;
    }

    public void setInterests(List<NodeInterest> interests) {
        this.interests = interests;
    }

    public List<NodeInterest> getInterests() {
        return interests;
    }
}
