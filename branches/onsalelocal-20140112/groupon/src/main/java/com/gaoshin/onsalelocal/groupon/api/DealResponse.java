package com.gaoshin.onsalelocal.groupon.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class DealResponse {
    private Deal deal;

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

}
