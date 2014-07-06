
package com.gaoshin.amazon.jax;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "itemOfferList"
})
@XmlRootElement(name = "ItemOfferList")
public class ItemOfferList {

    @XmlElement(name = "ItemOffer")
    protected List<ItemOffer> itemOfferList;

    public List<ItemOffer> getItemOfferList() {
        if (itemOfferList == null) {
        	itemOfferList = new ArrayList<ItemOffer>();
        }
        return this.itemOfferList;
    }

    public boolean hasOffer(String siteOrUrl) {
    	for(ItemOffer offer : getItemOfferList()) {
    		if(offer.isSameSite(siteOrUrl))
    			return true;
    	}
    	return false;
    }

    public ItemOffer getOffer(String siteOrUrl) {
    	for(ItemOffer offer : getItemOfferList()) {
    		if(offer.isSameSite(siteOrUrl))
    			return offer;
    	}
    	return null;
    }
}
