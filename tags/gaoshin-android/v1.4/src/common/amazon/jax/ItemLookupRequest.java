
package common.amazon.jax;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class ItemLookupRequest {
    protected String condition;
    protected String deliveryMethod;
    protected String futureLaunchDate;
    protected String idType;
    protected String ispuPostalCode;
    protected String merchantId;
    protected BigInteger offerPage;
    protected List<String> itemId;
    protected List<String> responseGroup;
    protected BigInteger reviewPage;
    protected String reviewSort;
    protected String searchIndex;
    protected String searchInsideKeywords;
    protected BigInteger tagPage;
    protected BigInteger tagsPerPage;
    protected String tagSort;
    protected String variationPage;
    protected String relatedItemPage;
    protected List<String> relationshipType;

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
     * Gets the value of the futureLaunchDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFutureLaunchDate() {
        return futureLaunchDate;
    }

    /**
     * Sets the value of the futureLaunchDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFutureLaunchDate(String value) {
        this.futureLaunchDate = value;
    }

    /**
     * Gets the value of the idType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdType() {
        return idType;
    }

    /**
     * Sets the value of the idType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdType(String value) {
        this.idType = value;
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
     * Gets the value of the offerPage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOfferPage() {
        return offerPage;
    }

    /**
     * Sets the value of the offerPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOfferPage(BigInteger value) {
        this.offerPage = value;
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
     * Gets the value of the reviewPage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReviewPage() {
        return reviewPage;
    }

    /**
     * Sets the value of the reviewPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReviewPage(BigInteger value) {
        this.reviewPage = value;
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
     * Gets the value of the searchIndex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchIndex() {
        return searchIndex;
    }

    /**
     * Sets the value of the searchIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchIndex(String value) {
        this.searchIndex = value;
    }

    /**
     * Gets the value of the searchInsideKeywords property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchInsideKeywords() {
        return searchInsideKeywords;
    }

    /**
     * Sets the value of the searchInsideKeywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchInsideKeywords(String value) {
        this.searchInsideKeywords = value;
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
     * Gets the value of the tagsPerPage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTagsPerPage() {
        return tagsPerPage;
    }

    /**
     * Sets the value of the tagsPerPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTagsPerPage(BigInteger value) {
        this.tagsPerPage = value;
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
     * Gets the value of the variationPage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariationPage() {
        return variationPage;
    }

    /**
     * Sets the value of the variationPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariationPage(String value) {
        this.variationPage = value;
    }

    /**
     * Gets the value of the relatedItemPage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelatedItemPage() {
        return relatedItemPage;
    }

    /**
     * Sets the value of the relatedItemPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelatedItemPage(String value) {
        this.relatedItemPage = value;
    }

    /**
     * Gets the value of the relationshipType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relationshipType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelationshipType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRelationshipType() {
        if (relationshipType == null) {
            relationshipType = new ArrayList<String>();
        }
        return this.relationshipType;
    }

}
