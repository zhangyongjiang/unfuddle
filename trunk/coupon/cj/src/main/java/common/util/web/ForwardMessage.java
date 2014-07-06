package common.util.web;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "", propOrder = {})
public class ForwardMessage {
    private String tourl;

    public void setTourl(String tourl) {
        this.tourl = tourl;
    }

    @XmlElement
    public String getTourl() {
        return tourl;
    }
}
