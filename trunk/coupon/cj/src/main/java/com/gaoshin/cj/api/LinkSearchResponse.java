package com.gaoshin.cj.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "cj-api")
public class LinkSearchResponse {
    private Links links;

    @XmlElement(name="links")
    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
