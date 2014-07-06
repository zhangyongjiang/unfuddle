package com.gaoshin.appbooster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class Prize extends DbEntity {
    @Column(nullable=false, length=64) private String name;
    @Column(nullable=false, length=1023) private String icon;
    @Column(nullable=false, length=1023) private String description;
    @Column(nullable=false) private int totalTickets;
    @Column(nullable=false) private int remainTickets;
    @Column(nullable=false) private int pointsPerTicket;
    @Column(nullable=false) private int start;
    @Column(nullable=false) private int end;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getRemainTickets() {
        return remainTickets;
    }

    public void setRemainTickets(int remainTickets) {
        this.remainTickets = remainTickets;
    }

    public int getPointsPerTicket() {
        return pointsPerTicket;
    }

    public void setPointsPerTicket(int pointsPerTicket) {
        this.pointsPerTicket = pointsPerTicket;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
