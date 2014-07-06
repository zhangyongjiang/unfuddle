package com.gaoshin.mandarin.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

public class Book {
    private List<Topic> topics = new ArrayList<Topic>();
    private List<String> basics = new ArrayList<String>();

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setBasics(List<String> basics) {
        this.basics = basics;
    }

    public List<String> getBasics() {
        return basics;
    }

    public List<Item> getBasicItems() {
        List<Item> list = new ArrayList<Item>();
        List<String> check = new ArrayList<String>();
        check.addAll(basics);
        for (Topic t : topics) {
            t.collect(check, list);
        }

        Collections.sort(list, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
        return list;
    }

    public Item getItem(String id) {
        for (Topic t : topics) {
            Item item = t.getItem(id);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    public void read(XmlResourceParser xpp) throws Exception {
        xpp.next();
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                String name = xpp.getName();
                if (name.equals("topic")) {
                    Topic topic = new Topic();
                    topics.add(topic);
                    topic.read(xpp);
                } else if (name.equals("basic")) {
                    readBasics(xpp);
                }
                break;
            }
            eventType = xpp.next();
        }
    }

    public void readBasics(XmlResourceParser xpp) throws Exception {
        xpp.next();
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                if (xpp.getName().equals("sid")) {
                    basics.add(xpp.nextText());
                }
                break;
            }
            eventType = xpp.next();
        }
    }

    public static List<Item> search(List<Item> original, String pattern) {
        if (pattern == null || pattern.trim().length() == 0) {
            return original;
        }

        pattern = pattern.toLowerCase();
        List<Item> newList = new ArrayList<Item>();
        for (Item item : original) {
            if (item.match(pattern))
                newList.add(item);
        }
        return newList;
    }

    public List<Item> searchBasic(String pattern) {
        return search(getBasicItems(), pattern);
    }
}
