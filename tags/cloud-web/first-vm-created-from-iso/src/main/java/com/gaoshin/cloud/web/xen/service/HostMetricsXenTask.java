package com.gaoshin.cloud.web.xen.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.xensource.xenapi.HostMetrics;


public class HostMetricsXenTask extends XenTask {
    private int cpus;
    private long memoryTotal;
    private long memoryFree;

    public HostMetricsXenTask(String url, String user, String pwd) {
        super(url, user, pwd);
    }

    @Override
    protected void doTask() throws Exception {
        Map<com.xensource.xenapi.Host, com.xensource.xenapi.Host.Record> hostRecs = com.xensource.xenapi.Host.getAllRecords(connection);
        for(Entry<com.xensource.xenapi.Host, com.xensource.xenapi.Host.Record> entry : hostRecs.entrySet()) {
            com.xensource.xenapi.Host xenhost = entry.getKey();
            com.xensource.xenapi.Host.Record record = entry.getValue();
            cpus = record.hostCPUs.size();
            Set<HostMetrics> allHostMetrics = record.metrics.getAll(connection);
            for(HostMetrics hm : allHostMetrics) {
                Map<HostMetrics, com.xensource.xenapi.HostMetrics.Record> hmRecords = hm.getAllRecords(connection);
                for(com.xensource.xenapi.HostMetrics.Record hmr : hmRecords.values()) {
                    memoryTotal = hmr.memoryTotal;
                    memoryFree = hmr.memoryFree;
                   break;
                }
            }
            break;
        }
    }

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
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
}
