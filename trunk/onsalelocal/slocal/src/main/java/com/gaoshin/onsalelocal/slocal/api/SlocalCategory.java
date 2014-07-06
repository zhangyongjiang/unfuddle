package com.gaoshin.onsalelocal.slocal.api;

import java.util.ArrayList;
import java.util.List;

public class SlocalCategory {
    private String name;
    private String id;
    private String link;
    private SlocalCategory parent;
    private List<SlocalCategory> children = new ArrayList<SlocalCategory>();

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

    public List<SlocalCategory> getChildren() {
        return children;
    }

    public void setChildren(List<SlocalCategory> children) {
        this.children = children;
    }

    public SlocalCategory getParent() {
        return parent;
    }

    public void setParent(SlocalCategory parent) {
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
