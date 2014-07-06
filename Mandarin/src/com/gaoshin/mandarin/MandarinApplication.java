package com.gaoshin.mandarin;

import org.xmlpull.v1.XmlPullParser;

import android.app.Application;
import android.content.res.XmlResourceParser;

import com.gaoshin.mandarin.data.Book;

public class MandarinApplication extends Application {
    private Book book;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() throws Exception {
        XmlResourceParser xpp = getResources().getXml(R.xml.script);
        xpp.next();
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT:
                book = new Book();
                break;
            case XmlPullParser.START_TAG:
                if (xpp.getName().equals("ec")) {
                    book.read(xpp);
                }
                break;
            }
            eventType = xpp.next();
        }
    }

    public Book getBook() {
        return book;
    }
}
