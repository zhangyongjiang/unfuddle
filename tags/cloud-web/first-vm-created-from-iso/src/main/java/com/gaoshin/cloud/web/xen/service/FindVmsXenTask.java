package com.gaoshin.cloud.web.xen.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import com.gaoshin.cloud.web.xen.bean.Vm;
import com.gaoshin.cloud.web.xen.bean.VmList;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VM.Record;


public class FindVmsXenTask extends XenTask {
    private Long hostId;
    private VmList vmList = new VmList();

    public FindVmsXenTask(String url, String user, String pwd) {
        super(url, user, pwd);
    }

    @Override
    protected void doTask() throws Exception {
        Map<VM, Record> all = VM.getAllRecords(connection);
        for(VM.Record record : all.values()) {
            Vm v = new Vm();
            v.setHostId(hostId);
            v.setMemoryDynamicMax(record.memoryDynamicMax);
            v.setMemoryDynamicMin(record.memoryDynamicMin);
            v.setMemoryStaticMax(record.memoryStaticMax);
            v.setMemoryStaticMin(record.memoryStaticMin);
            v.setMemoryTarget(record.memoryTarget);
            v.setNameDescription(record.nameDescription);
            v.setNameLabel(record.nameLabel);
            v.setPowerState(record.powerState);
            v.setPVArgs(record.PVArgs);
            v.setSnapshotTime(record.snapshotTime.getTime());
            v.setTemplate(record.isATemplate);
            v.setUserVersion(record.userVersion);
            v.setSnapshort(record.isASnapshot);
            v.setUuid(record.uuid);
            v.setVCPUsAtStartup(record.VCPUsAtStartup);
            v.setVCPUsMax(record.VCPUsMax);
            vmList.getItems().add(v);
        }
        Collections.sort(vmList.getItems(), new Comparator<Vm>(){
            @Override
            public int compare(Vm arg0, Vm arg1) {
                int i = arg1.getPowerState().compareTo(arg0.getPowerState());
                return (i != 0) ? i : arg0.getNameLabel().compareToIgnoreCase(arg1.getNameLabel());
            }});
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public VmList getVmList() {
        return vmList;
    }

    public void setVmList(VmList vmList) {
        this.vmList = vmList;
    }

}
