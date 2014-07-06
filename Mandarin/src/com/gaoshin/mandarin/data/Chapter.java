package com.gaoshin.mandarin.data;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

public class Chapter {
    private List<Item> items = new ArrayList<Item>();

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void read(XmlResourceParser xpp) throws Exception {
        xpp.next();
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                if (xpp.getName().equals("item")) {
                    Item child = new Item();
                    child.read(xpp);
                    items.add(child);
                }
                break;
            }
            eventType = xpp.next();
        }
    }

    public Item getItem(String id) {
        for (Item i : items) {
            if (id.equals(i.getId())) {
                return i;
            }
        }
        return null;
    }

    public void collect(List<String> basics, List<Item> list) {
        for (Item i : items) {
            if (basics.contains(i.getSid())) {
                list.add(i);
                basics.remove(i.getSid());
            }
        }
    }

}
