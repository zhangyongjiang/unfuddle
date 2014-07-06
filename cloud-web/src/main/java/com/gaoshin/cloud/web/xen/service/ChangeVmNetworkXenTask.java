package com.gaoshin.cloud.web.xen.service;

import java.util.Set;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VM.Record;

public class ChangeVmNetworkXenTask extends XenTask {

    private Long hostId;
    private String vmUuid;

    public ChangeVmNetworkXenTask(String url, String user, String pwd, Long hostId, String vmUuid) {
        super(url, user, pwd);
        this.hostId = hostId;
        this.vmUuid = vmUuid;
    }

    @Override
    protected void doTask() throws Exception {
        doTask(hostId, connection, vmUuid);
    }
    
    public void doTask(Long hostId, Connection connection, String vmUuid) throws Exception {
        VM vm = VM.getByUuid(connection, vmUuid);
        
        Set<Network> networks = Network.getAll(connection);
        Network net = null;
        for (Network network : networks)
        {
            com.xensource.xenapi.Network.Record record = network.getRecord(connection);
            String bridge = record.bridge;
            if("xenbr0".equals(bridge)) {
                net = network;
                break;
            }
        }

        Record record = vm.getRecord(connection);
        for(VIF vif : record.VIFs) {
            vif.destroy(connection);
        }
        makeVIF(vm, net, "0");
    }
    
    private VIF makeVIF(VM newVm, Network network, String device) throws Exception {
        VIF.Record newvifrecord = new VIF.Record();

        // These three parameters are used in the command line VIF creation
        newvifrecord.VM = newVm;
        newvifrecord.network = network;
        newvifrecord.device = device;
        newvifrecord.MTU = 1500L;

        return VIF.create(connection, newvifrecord);
    }
}
