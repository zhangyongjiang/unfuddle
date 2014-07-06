package com.gaoshin.cj.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "advertisers")
public class Advertisers {
    private int totalMatched;
    private int recordsReturned;
    private int pageNumber;
    private List<CjAdvertiser> advertiser = new ArrayList<CjAdvertiser>();
            
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

    @XmlElement(name="advertiser")
    public List<CjAdvertiser> getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(List<CjAdvertiser> advertiser) {
        this.advertiser = advertiser;
    }
}
