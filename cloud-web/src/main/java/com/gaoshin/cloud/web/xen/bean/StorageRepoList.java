package com.gaoshin.cloud.web.xen.bean;

import java.util.ArrayList;
import java.util.List;

public class StorageRepoList {
    private List<StorageRepo> items = new ArrayList<StorageRepo>();
    private long totalPhysicalSize;
    private long totalPhysicalUtilisation;

    public List<StorageRepo> getItems() {
        return items;
    }

    public void setItems(List<StorageRepo> items) {
        this.items = items;
    }

    public void sumup() {
        totalPhysicalSize = 0;
        totalPhysicalUtilisation = 0;
        for(StorageRepo sr : items) {
            totalPhysicalSize += sr.getPhysicalSize();
            totalPhysicalUtilisation += sr.getPhysicalUtilisation();
        }
    }

    public long getTotalPhysicalSize() {
        return totalPhysicalSize;
    }

    public void setTotalPhysicalSize(long totalPhysicalSize) {
        this.totalPhysicalSize = totalPhysicalSize;
    }

    public long getTotalPhysicalUtilisation() {
        return totalPhysicalUtilisation;
    }

    public void setTotalPhysicalUtilisation(long totalPhysicalUtilisation) {
        this.totalPhysicalUtilisation = totalPhysicalUtilisation;
    }
}
