package com.gaoshin.coupon.android.model;

import java.math.BigDecimal;

import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "Coupon",
        keyColumn = "_id",
        autoId = true,
        create = {
                "create table Coupon (" +
                        "_id INTEGER primary key autoincrement " +
                        ", id text " +
                        ", created bigint " +
                        ", updated bigint " +
                        ", storeId varchar(64) " +
                        ", createByUserId varchar(64) " +
                        ", title varchar(255) " +
                        ", description varchar(2048) " +
                        ", imageUrl varchar(1024) " +
                        ", targetUsers varchar(1024) " +
                        ", startFrom bigint " +
                        ", expire bigint " +
                        ", total integer " +
                        ", remain integer " +
                        ", parentId varchar(64) " +
                        ", status varchar(32) " +
                        ", numOfReviews integer " +
                        ", numOfRatings integer " +
                        ", totalRatings integer " +
                        ", barcode varchar(64) " +
                        ", promoCode varchar(64) " +
                        ", originalPrice varchar(32) " +
                        ", listPrice varchar(32) " +
                        ", validDate varchar(32) " +
                        ", category  varchar(32)" +
                        ", storeName varchar(255) " +
                        ", latitude real " +
                        ", longitude real " +
                        ", address varchar(255) " +
                        ", city varchar(255) " +
                        ", state varhchar(32) " +
                        ", country varchar(128) " +
                        ", zipcode varchar(16) " +
                        ", phone varchar(32) " +
                        ", url varchar(1024) " +
                        ", source varchar(64) " +
                        ")"
        })
public class Coupon {
    private Integer _id;
    private String id;
    private long created;
    private long updated;
    private String storeId;
    private String createByUserId;
    private String title;
    private String description;
    private String imageUrl;
    private String targetUsers;
    private long startFrom;
    private long expire;
    private int total;
    private int remain;
    private String parentId;
    private CouponStatus status;
    private int numOfReviews;
    private int numOfRatings;
    private int totalRatings;
    private String barcode;
    private String promoCode;
    private String originalPrice;
    private String listPrice;
    private String validDate;
    private String category;
    private String storeName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String phone;
    private String url;
    private String source;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCreateByUserId() {
        return createByUserId;
    }

    public void setCreateByUserId(String createByUserId) {
        this.createByUserId = createByUserId;
    }

    public String getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(String targetUsers) {
        this.targetUsers = targetUsers;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public long getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(long startFrom) {
        this.startFrom = startFrom;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }
}
