package com.gaoshin.cloud.web.xen.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gaoshin.cloud.web.bean.BusinessException;
import com.gaoshin.cloud.web.bean.ServiceError;
import com.gaoshin.cloud.web.xen.bean.CloneRequest;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VM.Record;

public class CloneVmXenTask extends XenTask {

    private String newVmUuid;
    private CloneRequest cloneRequest;

    public CloneVmXenTask(String url, String user, String pwd, CloneRequest cloneRequest) {
        super(url, user, pwd);
        this.cloneRequest = cloneRequest;
    }

    @Override
    protected void doTask() throws Exception {
        Map<VM, Record> all = VM.getAllRecords(connection);
        for(Entry<VM, Record> entry : all.entrySet()) {
            VM.Record record = entry.getValue();
            VM vm = entry.getKey();
            if(cloneRequest.getTemplateId().equals(record.uuid)) {
                if(Boolean.TRUE.equals(record.isATemplate)) {
                    createVm(vm);
                }
                else {
                    cloneVm(vm);
                }
                return;
            }
        }
        throw new BusinessException(ServiceError.InvalidInput, "cannot find vm template " + cloneRequest.getTemplateId());
    }
    
    private VM cloneVm(VM vm) throws Exception {
        VM newVm = vm.createClone(connection, cloneRequest.getVmName());
        newVmUuid = newVm.getUuid(connection);
        return newVm;
    }
    
    private VM createVm(VM vm) throws Exception {
        VM newVm = vm.createClone(connection, cloneRequest.getVmName());
        newVmUuid = newVm.getUuid(connection);
        
        Network network = getNetwork();
        makeVIF(newVm, network, "0");
        
        SR defaultSR = getDefaultSR();
        /* Put the SR uuid into the provision XML */
        Map<String, String> otherConfig = newVm.getOtherConfig(connection);
        String disks = otherConfig.get("disks");
        disks = disks.replace("sr=\"\"", "sr=\"" + defaultSR.getUuid(connection) + "\"");
        otherConfig.put("disks", disks);
        newVm.setOtherConfig(connection, otherConfig);
        
        VBD cdDrive = makeCDDrive(newVm);
        if(cloneRequest.getVdiUuid() != null) {
            newVm.addToOtherConfig(connection, "install-repository", "cdrom");
        }
        
        newVm.provision(connection);
        newVm.setIsATemplate(connection, false);
        
        // set default bvd non-bootable
        Set<VBD> vbDs = newVm.getVBDs(connection);
        for(VBD vbd : vbDs) {
            com.xensource.xenapi.VBD.Record vbdr = vbd.getRecord(connection);
            if("0".equals(vbdr.userdevice)) {
                vbd.setBootable(connection, false);
            }
        }
        
        return newVm;
    }
    
    protected Network getNetwork() throws Exception {
        Set<Network> networks = Network.getAll(connection);
        Network net = null;
        for (Network i : networks)
        {
            com.xensource.xenapi.Network.Record record = i.getRecord(connection);
            String bridge = record.bridge;
            if("xenbr0".equals(bridge)) {
                net = i;
            }
            System.out.println(bridge);
            
            bridge = record.nameDescription;
            System.out.println(bridge);
            
            bridge = record.nameLabel;
            System.out.println(bridge);
        }
        return net;
    }

    protected SR getDefaultSR() throws Exception {
        Set<Pool> pools = Pool.getAll(connection);
        Pool pool = (pools.toArray(new Pool[0]))[0];
        return pool.getDefaultSR(connection);
    }
    
    private VBD makeVBD(VM newVm) throws Exception {
        VBD.Record vbdc = new VBD.Record();

        // These three parameters are used in the command line VIF creation
        vbdc.VM = newVm;
        vbdc.device = "0";
        vbdc.bootable = false;
        vbdc.mode = Types.VbdMode.RW;
        vbdc.type = Types.VbdType.DISK;
        vbdc.unpluggable = true;

        return VBD.create(connection, vbdc);
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

    private VBD makeCDDrive(VM vm) throws Exception{
        VBD.Record vbdrecord = new VBD.Record();

        vbdrecord.VM = vm;
        vbdrecord.VDI = null;
        vbdrecord.userdevice = "3";
        vbdrecord.mode = Types.VbdMode.RO;
        vbdrecord.type = Types.VbdType.CD;
        vbdrecord.empty = true;
        vbdrecord.bootable = false;

        if(cloneRequest.getVdiUuid() != null) {
            VDI vdi = VDI.getByUuid(connection, cloneRequest.getVdiUuid());
            vbdrecord.VDI = vdi;
            vbdrecord.empty = false;
            vbdrecord.bootable = true;
        }

        return VBD.create(connection, vbdrecord);
    }
    
    public String getNewVmUuid() {
        return newVmUuid;
    }

    public void setNewVmUuid(String newVmUuid) {
        this.newVmUuid = newVmUuid;
    }

}
