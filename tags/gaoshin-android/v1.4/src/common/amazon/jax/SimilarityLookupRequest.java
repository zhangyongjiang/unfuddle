
package common.amazon.jax;

import java.util.ArrayList;
import java.util.List;

public class SimilarityLookupRequest {

    protected String condition;
    protected String deliveryMethod;
    protected List<String> itemId;
    protected String ispuPostalCode;
    protected String merchantId;
    protected List<String> responseGroup;
    protected String reviewSort;
    protected String similarityType;

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCondition(String value) {
        this.condition = value;
    }

    /**
     * Gets the value of the deliveryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Sets the value of the deliveryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryMethod(String value) {
        this.deliveryMethod = value;
    }

    /**
     * Gets the value of the itemId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getItemId() {
        if (itemId == null) {
            itemId = new ArrayList<String>();
        }
        return this.itemId;
    }

    /**
     * Gets the value of the ispuPostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISPUPostalCode() {
        return ispuPostalCode;
    }

    /**
     * Sets the value of the ispuPostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISPUPostalCode(String value) {
        this.ispuPostalCode = value;
    }

    /**
     * Gets the value of the merchantId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Sets the value of the merchantId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchantId(String value) {
        this.merchantId = value;
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

    /**
     * Gets the value of the reviewSort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReviewSort() {
        return reviewSort;
    }

    /**
     * Sets the value of the reviewSort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReviewSort(String value) {
        this.reviewSort = value;
    }

    /**
     * Gets the value of the similarityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimilarityType() {
        return similarityType;
    }

    /**
     * Sets the value of the similarityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimilarityType(String value) {
        this.similarityType = value;
    }

}
