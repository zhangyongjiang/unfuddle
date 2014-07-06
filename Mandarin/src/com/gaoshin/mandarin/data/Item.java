package com.gaoshin.mandarin.data;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

public class Item {
    private String id;
    private String sid;
    private String cscript;
    private String escript;
    private String cMaleAudio;
    private String cFemaleAudio;
    private String eAudio;
    private int order = Integer.MAX_VALUE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCscript() {
        return cscript;
    }

    public void setCscript(String cscript) {
        this.cscript = cscript;
    }

    public String getEscript() {
        return escript;
    }

    public void setEscript(String escript) {
        this.escript = escript;
    }

    public String getcMaleAudio() {
        return cMaleAudio;
    }

    public void setcMaleAudio(String cMaleAudio) {
        this.cMaleAudio = cMaleAudio;
    }

    public String getcFemaleAudio() {
        return cFemaleAudio;
    }

    public void setcFemaleAudio(String cFemaleAudio) {
        this.cFemaleAudio = cFemaleAudio;
    }

    public String geteAudio() {
        return eAudio;
    }

    public void seteAudio(String eAudio) {
        this.eAudio = eAudio;
    }

    public void read(XmlResourceParser xpp) throws Exception {
        xpp.next();
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            String name = xpp.getName();
            switch (eventType) {
            case XmlPullParser.START_TAG:
                if (name.equals("id")) {
                    setId(xpp.nextText());
                }
                if (name.equals("sid")) {
                    setSid(xpp.nextText());
                }
                if (name.equals("c")) {
                    setCscript(xpp.nextText());
                }
                if (name.equals("e")) {
                    setEscript(xpp.nextText());
                }
                if (name.equals("cm")) {
                    setcMaleAudio(xpp.nextText());
                }
                if (name.equals("cf")) {
                    setcFemaleAudio(xpp.nextText());
                }
                if (name.equals("au_e")) {
                    seteAudio(xpp.nextText());
                }
                if (name.equals("order")) {
                    setOrder(Integer.parseInt(xpp.nextText()));
                }
                break;
            }
            eventType = xpp.next();
        }
    }

    public boolean match(String pattern) {
        if (match(id, pattern))
            return true;
        if (match(sid, pattern))
            return true;
        if (match(cscript, pattern))
            return true;
        if (match(escript, pattern))
            return true;
        return false;
    }

    private boolean match(String s, String pattern) {
        if (s == null)
            return false;
        return s.toLowerCase().indexOf(pattern) != -1;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
