package com.gaoshin.cloud.web.xen.service;

import java.util.Map;
import java.util.Set;

import com.gaoshin.cloud.web.xen.bean.VirtualBlockDevice;
import com.gaoshin.cloud.web.xen.bean.VirtualBlockDeviceList;
import com.gaoshin.cloud.web.xen.bean.VirtualNetworkInterface;
import com.gaoshin.cloud.web.xen.bean.VirtualNetworkInterfaceList;
import com.gaoshin.cloud.web.xen.bean.VmDetails;
import com.gaoshin.cloud.web.xen.bean.XenConsole;
import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Console;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VM.Record;

public class VmDetailsXenTask extends XenTask {

    private Long hostId;
    private VmDetails vmDetails = new VmDetails();
    private String vmUuid;

    public VmDetailsXenTask(String url, String user, String pwd, Long hostId, String vmUuid) {
        super(url, user, pwd);
        this.hostId = hostId;
        this.vmUuid = vmUuid;
    }

    @Override
    protected void doTask() throws Exception {
        vmDetails = doTask(hostId, connection, vmUuid);
    }
    
    public static VmDetails doTask(Long hostId, Connection connection, String vmUuid) throws Exception {
        VmDetails vmDetails = new VmDetails();
        VM vm = VM.getByUuid(connection, vmUuid);
        Record record = vm.getRecord(connection);
        vmDetails.setHostId(hostId);
        vmDetails.setDomid(record.domid);
        vmDetails.setMemoryDynamicMax(record.memoryDynamicMax);
        vmDetails.setMemoryDynamicMin(record.memoryDynamicMin);
        vmDetails.setMemoryStaticMax(record.memoryStaticMax);
        vmDetails.setMemoryStaticMin(record.memoryStaticMin);
        vmDetails.setMemoryTarget(record.memoryTarget);
        vmDetails.setNameDescription(record.nameDescription);
        vmDetails.setNameLabel(record.nameLabel);
        vmDetails.setPowerState(record.powerState);
        vmDetails.setPVArgs(record.PVArgs);
        vmDetails.setSnapshotTime(record.snapshotTime.getTime());
        vmDetails.setTemplate(record.isATemplate);
        vmDetails.setUserVersion(record.userVersion);
        vmDetails.setSnapshort(record.isASnapshot);
        vmDetails.setUuid(record.uuid);
        vmDetails.setVCPUsAtStartup(record.VCPUsAtStartup);
        vmDetails.setVCPUsMax(record.VCPUsMax);
        
        VirtualBlockDeviceList vbdList = new VirtualBlockDeviceList();
        for(VBD vbd : record.VBDs) {
            com.xensource.xenapi.VBD.Record vbdRec = vbd.getRecord(connection);
            VirtualBlockDevice device = new VirtualBlockDevice();
            device.setBootable(vbdRec.bootable);
            device.setCurrentlyAttached(vbdRec.currentlyAttached);
            device.setDevice(vbdRec.device);
            device.setEmpty(vbdRec.empty);
            device.setStatusDetail(vbdRec.statusDetail);
            device.setType(vbdRec.type);
            device.setUnpluggable(vbdRec.unpluggable);
            device.setUuid(vbdRec.uuid);
            vbdList.getItems().add(device);
        }
        vmDetails.setVbdList(vbdList.getItems());
        
        VirtualNetworkInterfaceList vifList = new VirtualNetworkInterfaceList();
        for(VIF vif : record.VIFs) {
            com.xensource.xenapi.VIF.Record vifRec = vif.getRecord(connection);
            VirtualNetworkInterface net = new VirtualNetworkInterface();
            vifList.getItems().add(net);
            net.setCurrentlyAttached(vifRec.currentlyAttached);
            net.setDevice(vifRec.device);
            net.setMac(vifRec.MAC);
            net.setStatusDetail(vifRec.statusDetail);
            net.setUuid(vifRec.uuid);
        }
        vmDetails.setVirtualNetworkInterfaceList(vifList.getItems());
        
        try {
            System.out.println(record.otherConfig);
            Map<String, String> config = record.metrics.getOtherConfig(connection);
            System.out.println(config.toString());
            Map<String, String> networks = record.guestMetrics.getNetworks(connection);
            String ip = networks.get("0/ip");
            vmDetails.setIpAddress(ip);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            Set<Console> consoles = vm.getConsoles(connection);
            for(Console console : consoles) {
                com.xensource.xenapi.Console.Record conRec = console.getRecord(connection);
                XenConsole xenConsole = new XenConsole();
                xenConsole.setLocation(conRec.location);
                xenConsole.setUuid(conRec.uuid);
                xenConsole.setHostId(hostId);
                xenConsole.setVmId(record.uuid);
                vmDetails.getConsoleList().add(xenConsole);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return vmDetails;
    }

    public VmDetails getVmDetails() {
        return vmDetails;
    }

    public void setVmDetails(VmDetails vmDetails) {
        this.vmDetails = vmDetails;
    }

}
