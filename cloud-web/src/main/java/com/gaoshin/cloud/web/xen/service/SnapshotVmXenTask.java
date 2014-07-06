package com.gaoshin.cloud.web.xen.service;

import com.gaoshin.cloud.web.xen.bean.SnapshotRequest;
import com.xensource.xenapi.VM;

public class SnapshotVmXenTask extends XenTask {

    private String newVmUuid;
    private SnapshotRequest request;

    public SnapshotVmXenTask(String url, String user, String pwd, SnapshotRequest request) {
        super(url, user, pwd);
        this.request = request;
    }

    @Override
    protected void doTask() throws Exception {
        VM vm = VM.getByUuid(connection, request.getVmId());
        VM snapshot = vm.snapshot(connection, request.getSnapshotName());
        newVmUuid = snapshot.getUuid(connection);
    }

    public String getNewVmUuid() {
        return newVmUuid;
    }
}
