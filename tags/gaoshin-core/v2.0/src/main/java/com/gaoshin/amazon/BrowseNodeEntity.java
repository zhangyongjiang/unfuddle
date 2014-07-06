package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import common.util.reflection.ReflectionUtil;

@Entity
@Table(name = "aws_node")
public class BrowseNodeEntity {
    @Id
    @Column(length = 32)
    private String id;

    @Column(length = 255)
    private String name;

    @Column(nullable = false)
    private boolean root;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "aws_node_tree",
            joinColumns = { @JoinColumn(name = "NODE_ID") },
            inverseJoinColumns = { @JoinColumn(name = "CHILD_ID") })
    private List<BrowseNodeEntity> children = new ArrayList<BrowseNodeEntity>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "children")
    private List<BrowseNodeEntity> parents = new ArrayList<BrowseNodeEntity>();

    @Column(nullable = false)
    private boolean loaded = false;

    @Column(nullable = false, name = "bad_node")
    private boolean badNode = false;

    @Column(nullable = false)
    private int retry = 0;

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Column(name = "itemed")
    private boolean itemed = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "aws_top_item",
            joinColumns = { @JoinColumn(name = "NODE_ID") },
            inverseJoinColumns = { @JoinColumn(name = "asin") })
    private List<ItemEntity> topItems = new ArrayList<ItemEntity>();

    public BrowseNodeEntity() {
    }

    public BrowseNodeEntity(Object bean) {
        copyFrom(bean);
    }

    public void copyFrom(Object bean) {
        ReflectionUtil.copyPrimeProperties(this, bean);
    }

    public AwsBrowseNode getBean() {
        try {
            AwsBrowseNode bean = new AwsBrowseNode();
            ReflectionUtil.copyPrimeProperties(bean, this);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public List<BrowseNodeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<BrowseNodeEntity> children) {
        this.children = children;
    }

    public List<BrowseNodeEntity> getParents() {
        return parents;
    }

    public void setParents(List<BrowseNodeEntity> parents) {
        this.parents = parents;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean hasChild(String cid) {
        for (BrowseNodeEntity child : children) {
            if (child.getId().equals(cid))
                return true;
        }
        return false;
    }

    public boolean hasParent(String pid) {
        for (BrowseNodeEntity parent : parents) {
            if (parent.getId().equals(pid))
                return true;
        }
        return false;
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

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void resetLastUpdate() {
        lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public void setItemed(boolean itemed) {
        this.itemed = itemed;
    }

    public boolean isItemed() {
        return itemed;
    }

    public void setTopItems(List<ItemEntity> topItems) {
        this.topItems = topItems;
    }

    public List<ItemEntity> getTopItems() {
        return topItems;
    }
}
