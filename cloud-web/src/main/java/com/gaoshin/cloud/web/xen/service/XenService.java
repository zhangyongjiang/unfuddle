package com.gaoshin.cloud.web.xen.service;

import com.gaoshin.cloud.web.xen.bean.CloneRequest;
import com.gaoshin.cloud.web.xen.bean.ConsoleSession;
import com.gaoshin.cloud.web.xen.bean.Host;
import com.gaoshin.cloud.web.xen.bean.HostDetails;
import com.gaoshin.cloud.web.xen.bean.HostList;
import com.gaoshin.cloud.web.xen.bean.HostNetworkList;
import com.gaoshin.cloud.web.xen.bean.SnapshotRequest;
import com.gaoshin.cloud.web.xen.bean.StorageRepoDetails;
import com.gaoshin.cloud.web.xen.bean.StorageRepoList;
import com.gaoshin.cloud.web.xen.bean.VirtualDiskImageList;
import com.gaoshin.cloud.web.xen.bean.VmDetails;
import com.gaoshin.cloud.web.xen.bean.VmList;


public interface XenService {

    Host createHost(Host host);

    HostList listHosts();

    HostDetails getHostDetails(Long hostId);

    void updateHost(Host host);

    void removeHost(Long hostId);

    VmList listHostVms(Long hostId) throws Exception;

    String cloneVm(CloneRequest cloneRequest);

    void startVm(Long hostId, String vmId);

    void shutdownVm(Long hostId, String vmId);

    void suspendVm(Long hostId, String vmId);

    VmDetails getVmDetails(Long hostId, String vmId);

    void resumeVm(Long hostId, String vmId);

    void destroyVm(Long hostId, String vmId);

    ConsoleSession getConsole(Long hostId, String vmId, String consoleId) throws Exception;

    void sessionHeartBeat(String sessionId);

    Host getHost(Long hostId);

    void refreshHostStorageRepository(Long hostId) throws Exception;

    void refreshHostVirtualDiskImages(Long hostId) throws Exception;

    StorageRepoDetails getStorageRepoDetails(Long hostId, String srUuid) throws Exception;

    VirtualDiskImageList listHostVdis(Long hostId) throws Exception;

    StorageRepoList listHostStorage(Long hostId) throws Exception;

    HostNetworkList listNetwork(Long hostId) throws Exception;

    String snapshotVm(SnapshotRequest request);

    void changeNetwork(Long hostId, String vmId);
}
