package com.gaoshin.onsalelocal.search;

public class IndexedPlace {
    private String id;
    private String text;
    private Double latitude;
    private Double longitude;
    private String name;
    private String img_url;
    private String category;
    private String cat2;
    private String cat3;
    private Integer raves;
    private String formatted_name;
    private String formatted_street;
    private String address;
    private String city;
    private String state;
    private String country;
    private String phone;
    private Long updated;
    private Integer popularity;
    private int similarity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public Integer getRaves() {
        return raves;
    }

    public void setRaves(Integer raves) {
        this.raves = raves;
    }

    public String getFormatted_name() {
        return formatted_name;
    }

    public void setFormatted_name(String formatted_name) {
        this.formatted_name = formatted_name;
    }

    public String getFormatted_street() {
        return formatted_street;
    }

    public void setFormatted_street(String formatted_street) {
        this.formatted_street = formatted_street;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return similarity + "\t" + phone + "\t" + name + "\t" + address + "\t" + city + "\t" + state + "\t"; 
    }
}
