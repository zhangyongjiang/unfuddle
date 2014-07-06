package com.gaoshin.mandarin.data;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

public class Topic extends Item {
    private List<Chapter> chapters = new ArrayList<Chapter>();

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void read(XmlResourceParser xpp) throws Exception {
        xpp.next();
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                if (xpp.getName().equals("item")) {
                    super.read(xpp);
                } else if (xpp.getName().equals("chapter")) {
                    Chapter chapter = new Chapter();
                    chapter.read(xpp);
                    chapters.add(chapter);
                }
                break;
            }
            eventType = xpp.next();
        }
    }

    public Item getItem(String id) {
        for (Chapter c : chapters) {
            Item item = c.getItem(id);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    public void collect(List<String> basics, List<Item> list) {
        for (Chapter c : chapters) {
            c.collect(basics, list);
        }
    }
}
