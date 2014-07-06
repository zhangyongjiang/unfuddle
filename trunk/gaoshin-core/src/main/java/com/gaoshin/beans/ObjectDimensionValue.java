package com.gaoshin.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ObjectDimensionValue {
    private Long objId;
    private Long dimValue;

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public Long getObjId() {
        return objId;
    }

    public void setDimValue(Long dimValue) {
        this.dimValue = dimValue;
    }

    public Long getDimValue() {
        return dimValue;
    }
}
