package com.gaoshin.groupon.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class DivisionsResponse {
    private DivisionList divisions;

    public DivisionList getDivisions() {
        return divisions;
    }

    public void setDivisions(DivisionList divisions) {
        this.divisions = divisions;
    }
}
