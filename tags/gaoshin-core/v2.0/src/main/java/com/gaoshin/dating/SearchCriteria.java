package com.gaoshin.dating;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.beans.Gender;

@XmlRootElement
public class SearchCriteria {
    private Long id;
    private DimensionRace[] race;
    private DimensionIncome[] income;
    private DimensionSmoking[] smoking;
    private DimensionDrinking[] drinking;
    private DimensionMarriageStatus[] marriage;
    private DimensionLanguage[] language;
    private DimensionEducation[] education;
    private DimensionLikeChildren[] likekids;
    private DimensionPets[] pets;
    private Gender[] gender;
    private String country;
    private Integer minHeight;
    private Integer maxHeight;
    private Integer maxKids;
    private Integer minWeight;
    private Integer maxWeight;
    private Integer minBirthYear;
    private Integer maxBirthYear;

    public DimensionRace[] getRace() {
        return race;
    }

    public void setRace(DimensionRace[] race) {
        this.race = race;
    }

    public DimensionIncome[] getIncome() {
        return income;
    }

    public void setIncome(DimensionIncome[] income) {
        this.income = income;
    }

    public DimensionSmoking[] getSmoking() {
        return smoking;
    }

    public void setSmoking(DimensionSmoking[] smoking) {
        this.smoking = smoking;
    }

    public DimensionDrinking[] getDrinking() {
        return drinking;
    }

    public void setDrinking(DimensionDrinking[] drinking) {
        this.drinking = drinking;
    }

    public DimensionMarriageStatus[] getMarriage() {
        return marriage;
    }

    public void setMarriage(DimensionMarriageStatus[] marriage) {
        this.marriage = marriage;
    }

    public DimensionLanguage[] getLanguage() {
        return language;
    }

    public void setLanguage(DimensionLanguage[] language) {
        this.language = language;
    }

    public DimensionEducation[] getEducation() {
        return education;
    }

    public void setEducation(DimensionEducation[] education) {
        this.education = education;
    }

    public DimensionLikeChildren[] getLikekids() {
        return likekids;
    }

    public void setLikekids(DimensionLikeChildren[] likekids) {
        this.likekids = likekids;
    }

    public DimensionPets[] getPets() {
        return pets;
    }

    public void setPets(DimensionPets[] pets) {
        this.pets = pets;
    }

    public Gender[] getGender() {
        return gender;
    }

    public void setGender(Gender[] gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getMaxKids() {
        return maxKids;
    }

    public void setMaxKids(Integer maxKids) {
        this.maxKids = maxKids;
    }

    public Integer getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getMinBirthYear() {
        return minBirthYear;
    }

    public void setMinBirthYear(Integer minBirthYear) {
        this.minBirthYear = minBirthYear;
    }

    public Integer getMaxBirthYear() {
        return maxBirthYear;
    }

    public void setMaxBirthYear(Integer maxBirthYear) {
        this.maxBirthYear = maxBirthYear;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
