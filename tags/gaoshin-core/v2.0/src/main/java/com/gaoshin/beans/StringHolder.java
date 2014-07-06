package com.gaoshin.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringHolder {
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
