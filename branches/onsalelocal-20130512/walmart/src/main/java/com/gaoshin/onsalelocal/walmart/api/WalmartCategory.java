package com.gaoshin.onsalelocal.walmart.api;

import java.util.ArrayList;
import java.util.List;

public class WalmartCategory {
    private String name;
    private String id;
    private String link;
    private WalmartCategory parent;
    private List<WalmartCategory> children = new ArrayList<WalmartCategory>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<WalmartCategory> getChildren() {
        return children;
    }

    public void setChildren(List<WalmartCategory> children) {
        this.children = children;
    }

    public WalmartCategory getParent() {
        return parent;
    }

    public void setParent(WalmartCategory parent) {
        this.parent = parent;
    }

    public boolean equals(String name) {
        if(this.name != null && this.name.equals(name))
            return true;
        if(parent == null)
            return false;
        return parent.equals(name);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\t").append(id).append("\t").append(link).append("\t").append(parent == null ? "null" : parent.getId());
        return sb.toString();
    }
}
