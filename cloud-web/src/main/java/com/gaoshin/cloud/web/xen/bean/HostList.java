package com.gaoshin.cloud.web.xen.bean;

import java.util.ArrayList;
import java.util.List;

public class HostList {
    private List<Host> items = new ArrayList<Host>();
    private int cpus;
    private long memoryTotal;
    private long memoryFree;

    public List<Host> getItems() {
        return items;
    }

    public void setItems(List<Host> items) {
        this.items = items;
    }

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cups) {
        this.cpus = cups;
    }

    public long getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(long memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public long getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(long memoryFree) {
        this.memoryFree = memoryFree;
    }
    
    public void sumup() {
        int cpus = 0;
        long memoryFree = 0;
        long memoryTotal = 0;
        for(Host he : items) {
            cpus += he.getCpus();
            memoryFree += he.getMemoryFree();
            memoryTotal += he.getMemoryTotal();
        }
        setCpus(cpus);
        setMemoryFree(memoryFree);
        setMemoryTotal(memoryTotal);
    }
}
