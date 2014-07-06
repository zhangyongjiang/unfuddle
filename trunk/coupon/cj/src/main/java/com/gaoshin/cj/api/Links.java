package com.gaoshin.cj.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "links")
public class Links {
    private int totalMatched;
    private int recordsReturned;
    private int pageNumber;
    private List<CjLink> link = new ArrayList<CjLink>();
            
    @XmlElement(name="link")
    public List<CjLink> getCjLink() {
        return link;
    }

    public void setCjLink(List<CjLink> link) {
        this.link = link;
    }

    @XmlAttribute(name="total-matched")
    public int getTotalMatched() {
        return totalMatched;
    }

    public void setTotalMatched(int totalMatched) {
        this.totalMatched = totalMatched;
    }

    @XmlAttribute(name="records-returned")
    public int getRecordsReturned() {
        return recordsReturned;
    }

    public void setRecordsReturned(int recordsReturned) {
        this.recordsReturned = recordsReturned;
    }

    @XmlAttribute(name="page-number")
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
