package com.gaoshin.onsalelocal.api;

import java.util.ArrayList;
import java.util.List;

public class CategoryBean extends Category {
	private List<CategoryBean> items = new ArrayList<CategoryBean>();
	
    public List<CategoryBean> getItems() {
        return items;
    }

    public void setItems(List<CategoryBean> items) {
        this.items = items;
    }

    public CategoryBean search(String id) {
        for(CategoryBean cat : items) {
            if(cat.getId().equals(id))
                return cat;
        }
        return null;
    }


}
