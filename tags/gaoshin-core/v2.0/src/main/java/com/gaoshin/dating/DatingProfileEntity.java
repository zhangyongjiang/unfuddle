package com.gaoshin.dating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.gaoshin.beans.Gender;
import com.gaoshin.entity.GenericEntity;
import com.gaoshin.entity.UserEntity;

@Entity
@Table(name = "xo_profile")
public class DatingProfileEntity extends GenericEntity {
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "lfor")
    @Enumerated(EnumType.ORDINAL)
    private DimensionLookingFor lookingFor;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionRace race;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionJob job;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionIncome income;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionLikeChildren likekids;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionPets pets;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionSmoking smoking;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionDrinking drinking;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionMarriageStatus marriage;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionLanguage language0;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionLanguage language1;

    @Column(length = 128)
    private String country;

    @Column
    private int height;

    @Column
    private int kids;

    @Column
    private int weight;

    @Column(length = 127)
    private String interest0;

    @Column(length = 127)
    private String interest1;

    @Column(length = 127)
    private String interest2;

    @Column(length = 1023)
    private String introduction;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "byear")
    private Integer birthYear;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private DimensionEducation education;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public DimensionLookingFor getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(DimensionLookingFor lookingFor) {
        this.lookingFor = lookingFor;
    }

    public DimensionRace getRace() {
        return race;
    }

    public void setRace(DimensionRace race) {
        this.race = race;
    }

    public DimensionJob getJob() {
        return job;
    }

    public void setJob(DimensionJob job) {
        this.job = job;
    }

    public DimensionIncome getIncome() {
        return income;
    }

    public void setIncome(DimensionIncome income) {
        this.income = income;
    }

    public DimensionLikeChildren getLikekids() {
        return likekids;
    }

    public void setLikekids(DimensionLikeChildren likekids) {
        this.likekids = likekids;
    }

    public DimensionPets getPets() {
        return pets;
    }

    public void setPets(DimensionPets pets) {
        this.pets = pets;
    }

    public DimensionSmoking getSmoking() {
        return smoking;
    }

    public void setSmoking(DimensionSmoking smoking) {
        this.smoking = smoking;
    }

    public DimensionDrinking getDrinking() {
        return drinking;
    }

    public void setDrinking(DimensionDrinking drinking) {
        this.drinking = drinking;
    }

    public DimensionMarriageStatus getMarriage() {
        return marriage;
    }

    public void setMarriage(DimensionMarriageStatus marriage) {
        this.marriage = marriage;
    }

    public DimensionLanguage getLanguage0() {
        return language0;
    }

    public void setLanguage0(DimensionLanguage language0) {
        this.language0 = language0;
    }

    public DimensionLanguage getLanguage1() {
        return language1;
    }

    public void setLanguage1(DimensionLanguage language1) {
        this.language1 = language1;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getInterest0() {
        return interest0;
    }

    public void setInterest0(String interest0) {
        this.interest0 = interest0;
    }

    public String getInterest1() {
        return interest1;
    }

    public void setInterest1(String interest1) {
        this.interest1 = interest1;
    }

    public String getInterest2() {
        return interest2;
    }

    public void setInterest2(String interest2) {
        this.interest2 = interest2;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setKids(int kids) {
        this.kids = kids;
    }

    public int getKids() {
        return kids;
    }

    public void setEducation(DimensionEducation education) {
        this.education = education;
    }

    public DimensionEducation getEducation() {
        return education;
    }

}
