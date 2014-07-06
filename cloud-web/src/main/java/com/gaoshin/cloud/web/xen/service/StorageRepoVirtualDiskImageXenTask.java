package com.gaoshin.cloud.web.xen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gaoshin.cloud.web.xen.bean.VirtualDiskImage;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VDI.Record;


public class StorageRepoVirtualDiskImageXenTask extends XenTask {
    private Long hostId;
    private List<VirtualDiskImage> items = new ArrayList<VirtualDiskImage>();
    private String srUuid;

    public StorageRepoVirtualDiskImageXenTask(String url, String user, String pwd, Long hostId, String srUuid) {
        super(url, user, pwd);
        this.hostId = hostId;
        this.srUuid = srUuid;
    }

    @Override
    protected void doTask() throws Exception {
        SR sr = SR.getByUuid(connection, srUuid);
        
        Set<VDI> all = sr.getVDIs(connection);
        for(VDI vdi : all) {
            Record rec = vdi.getRecord(connection);
            VirtualDiskImage bean = new VirtualDiskImage();
            bean.setHostId(hostId);
            bean.setIsASnapshot(rec.isASnapshot);
            bean.setLocation(rec.location);
            bean.setManaged(rec.managed);
            bean.setMissing(rec.missing);
            bean.setNameDescription(rec.nameDescription);
            bean.setNameLabel(rec.nameLabel);
            bean.setOtherConfig(rec.otherConfig);
            bean.setPhysicalUtilisation(rec.physicalUtilisation);
            bean.setReadOnly(rec.readOnly);
            bean.setSharable(rec.sharable);
            bean.setSmConfig(rec.smConfig);
            bean.setSnapshotTime(rec.snapshotTime == null ? null : rec.snapshotTime.getTime());
            bean.setStorageLock(rec.storageLock);
            bean.setTags(rec.tags);
            bean.setType(rec.type);
            bean.setUuid(rec.uuid);
            bean.setVirtualSize(rec.virtualSize);
            bean.setXenstoreData(rec.xenstoreData);

            try {
                bean.setParentVdiUuid(rec.parent == null ? null : rec.parent.getUuid(connection));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                bean.setStorageReopUuid(rec.SR == null ? null : rec.SR.getUuid(connection));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            items.add(bean);
        }
    }

    public List<VirtualDiskImage> getItems() {
        return items;
    }

    public void setItems(List<VirtualDiskImage> items) {
        this.items = items;
    }
    
}
