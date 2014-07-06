package com.gaoshin.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LocationDistance extends Location {
    private Double distance;

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }
}
