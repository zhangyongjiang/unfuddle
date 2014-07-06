package com.gaoshin.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DimensionValue {
    private Long dimvalue;
    private String dvalue;
    private String dvalue2;
    private String dvalue3;
    private Dimension dimension;

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimvalue(Long dimvalue) {
        this.dimvalue = dimvalue;
    }

    public Long getDimvalue() {
        return dimvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue2(String dvalue2) {
        this.dvalue2 = dvalue2;
    }

    public String getDvalue2() {
        return dvalue2;
    }

    public void setDvalue3(String dvalue3) {
        this.dvalue3 = dvalue3;
    }

    public String getDvalue3() {
        return dvalue3;
    }
}
