package com.gaoshin.dating;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.beans.Gender;
import com.gaoshin.beans.User;

@XmlRootElement
public class DatingProfile {
    private Long id;
    private User user;
    private DimensionLookingFor lookingFor;
    private DimensionRace race;
    private DimensionJob job;
    private DimensionIncome income;
    private DimensionLikeChildren likekids;
    private DimensionPets pets;
    private DimensionSmoking smoking;
    private DimensionDrinking drinking;
    private DimensionMarriageStatus marriage;
    private DimensionLanguage language0;
    private DimensionLanguage language1;
    private String country;
    private int height;
    private int kids;
    private int weight;
    private String interest0;
    private String interest1;
    private String interest2;
    private String introduction;
    private Gender gender;
    private Integer birthYear;
    private DimensionEducation education;

    public int getCompleteness() {
        int count = 0;
        int total = 0;

        total++;
        if (lookingFor != null)
            count++;

        total++;
        if (race != null)
            count++;

        total++;
        if (job != null)
            count++;

        total++;
        if (income != null)
            count++;

        total++;
        if (likekids != null)
            count++;

        total++;
        if (pets != null)
            count++;

        total++;
        if (smoking != null)
            count++;

        total++;
        if (drinking != null)
            count++;

        total++;
        if (marriage != null)
            count++;

        total++;
        if (language0 != null || language1 != null)
            count++;

        total++;
        if (height > 0)
            count++;

        total++;
        if (weight > 0)
            count++;

        total++;
        if (interest0 != null || interest1 != null || interest2 != null)
            count++;

        total++;
        if (introduction != null)
            count++;

        total++;
        if (gender != null)
            count++;

        total++;
        if (birthYear != null)
            count++;

        total++;
        if (education != null)
            count++;

        return count * 100 / total;
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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - birthYear;
    }

    public void setEducation(DimensionEducation education) {
        this.education = education;
    }

    public DimensionEducation getEducation() {
        return education;
    }

}
