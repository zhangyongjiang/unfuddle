
package common.amazon.jax;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TagLookupRequest {
    protected List<String> tagName;
    protected String customerId;
    protected BigInteger tagPage;
    protected BigInteger count;
    protected String tagSort;
    protected List<String> responseGroup;

    /**
     * Gets the value of the tagName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tagName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTagName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTagName() {
        if (tagName == null) {
            tagName = new ArrayList<String>();
        }
        return this.tagName;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
    }

    /**
     * Gets the value of the tagPage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTagPage() {
        return tagPage;
    }

    /**
     * Sets the value of the tagPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTagPage(BigInteger value) {
        this.tagPage = value;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCount(BigInteger value) {
        this.count = value;
    }

    /**
     * Gets the value of the tagSort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTagSort() {
        return tagSort;
    }

    /**
     * Sets the value of the tagSort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTagSort(String value) {
        this.tagSort = value;
    }

    /**
     * Gets the value of the responseGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the responseGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResponseGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getResponseGroup() {
        if (responseGroup == null) {
            responseGroup = new ArrayList<String>();
        }
        return this.responseGroup;
    }

}
