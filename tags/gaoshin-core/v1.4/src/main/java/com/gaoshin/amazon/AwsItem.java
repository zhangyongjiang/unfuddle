package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AwsItem {
    private String asin;
    private String title;
    private boolean loaded = false;
    private Calendar lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private List<ItemInterest> listItemInterest = new ArrayList<ItemInterest>();

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getAsin() {
        return asin;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setListItemInterest(List<ItemInterest> listItemInterest) {
        this.listItemInterest = listItemInterest;
    }

    public List<ItemInterest> getListItemInterest() {
        return listItemInterest;
    }

    public List<ItemInterest> getListBuyer() {
        List<ItemInterest> list = new ArrayList<ItemInterest>();
        for (ItemInterest item : listItemInterest) {
            if (!item.isSell()) {
                list.add(item);
            }
        }
        return list;
    }

    public List<ItemInterest> getListSeller() {
        List<ItemInterest> list = new ArrayList<ItemInterest>();
        for (ItemInterest item : listItemInterest) {
            if (item.isSell()) {
                list.add(item);
            }
        }
        return list;
    }
}
