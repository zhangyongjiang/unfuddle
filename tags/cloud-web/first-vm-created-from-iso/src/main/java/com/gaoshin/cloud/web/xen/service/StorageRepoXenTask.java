package com.gaoshin.cloud.web.xen.service;

import java.util.Map;
import java.util.Map.Entry;

import com.gaoshin.cloud.web.xen.bean.StorageRepo;
import com.gaoshin.cloud.web.xen.bean.StorageRepoList;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.SR.Record;

public class StorageRepoXenTask extends XenTask {
    private StorageRepoList repos = new StorageRepoList();
    private Long hostId;

    public StorageRepoXenTask(String url, String user, String pwd, Long hostId) {
        super(url, user, pwd);
        this.hostId = hostId;
    }

    @Override
    protected void doTask() throws Exception {
        Map<SR, Record> allRecords = SR.getAllRecords(connection);
        for(Entry<SR, Record> entry : allRecords.entrySet()) {
            Record rec = entry.getValue();
            SR s = entry.getKey();
            StorageRepo sr = new StorageRepo();
            sr.setDescription(rec.nameDescription);
            sr.setName(rec.nameLabel);
            sr.setPhysicalSize(rec.physicalSize);
            sr.setPhysicalUtilisation(rec.physicalUtilisation);
            sr.setType(rec.type);
            sr.setUuid(rec.uuid);
            sr.setShared(rec.shared);
            sr.setHostId(hostId);
            
            repos.getItems().add(sr);
        }
        repos.sumup();
    }

    public StorageRepoList getRepos() {
        return repos;
    }

    public void setRepos(StorageRepoList repos) {
        this.repos = repos;
    }
}
