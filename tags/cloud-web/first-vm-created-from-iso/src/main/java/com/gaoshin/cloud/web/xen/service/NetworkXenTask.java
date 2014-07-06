package com.gaoshin.cloud.web.xen.service;

import java.util.Map;
import java.util.Map.Entry;

import com.gaoshin.cloud.web.xen.bean.HostNetwork;
import com.gaoshin.cloud.web.xen.bean.HostNetworkList;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.Network.Record;

public class NetworkXenTask extends XenTask {
    private HostNetworkList networks = new HostNetworkList();
    private Long hostId;

    public NetworkXenTask(String url, String user, String pwd, Long hostId) {
        super(url, user, pwd);
        this.hostId = hostId;
    }

    @Override
    protected void doTask() throws Exception {
        Map<Network, Record> allRecords = Network.getAllRecords(connection);
        for(Entry<Network, Record> entry : allRecords.entrySet()) {
            Record rec = entry.getValue();
            Network s = entry.getKey();
            HostNetwork hn = new HostNetwork();
            hn.setUuid(rec.uuid);
            hn.setBridge(rec.bridge);
            hn.setNameLabel(rec.nameLabel);
            hn.setNameDescription(rec.nameDescription);
            networks.getItems().add(hn);
        }
    }

    public HostNetworkList getNetworks() {
        return networks;
    }

    public void setNetworks(HostNetworkList networks) {
        this.networks = networks;
    }
}
