package com.gaoshin.appbooster.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.appbooster.entity.Reward;

@XmlRootElement
public class RewardList {
    private List<Reward> items = new ArrayList<Reward>();

    public List<Reward> getItems() {
        return items;
    }

    public void setItems(List<Reward> items) {
        this.items = items;
    }
}
