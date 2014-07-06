package com.gaoshin.appbooster.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.appbooster.entity.User;

@XmlRootElement
public class UserDetails extends User {
    private ApplicationDetailsList applicationDetailsList = new ApplicationDetailsList();

    public ApplicationDetailsList getApplicationDetailsList() {
        return applicationDetailsList;
    }

    public void setApplicationDetailsList(ApplicationDetailsList applicationDetailsList) {
        this.applicationDetailsList = applicationDetailsList;
    }
}
