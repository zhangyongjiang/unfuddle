package com.gaoshin.coupon.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.coupon.entity.Store;

@XmlRootElement
public class StoreTree extends Store {
    private StoreTree parent;
    private List<StoreTree> children = new ArrayList<StoreTree>();

    public boolean hasParent(String id) {
        if(getParent() == null)
            return false;
        if(getParent().getId().equals(id)) {
            return true;
        }
        return getParent().hasParent(id);
    }
    
    public boolean hasChild(String id) {
        if(children == null || children.size() == 0)
            return false;
        
        for(StoreTree child : children) {
            if(child.getId().equals(id))
                return true;
            if(child.hasChild(id))
                return true;
        }
        
        return false;
    }
    
    public boolean contains(String id) {
        if(id.equals(getId()))
            return true;
        
        if(getParent() != null && getParent().contains(id))
            return true;
        
        if(getChildren() != null) {
            for(StoreTree child : getChildren()) {
                if(child.contains(id))
                    return true;
            }
        }
        
        return false;
    }

    public StoreTree getParent() {
        return parent;
    }

    public void setParent(StoreTree parent) {
        this.parent = parent;
    }

    public List<StoreTree> getChildren() {
        return children;
    }

    public void setChildren(List<StoreTree> children) {
        this.children = children;
    }
}
