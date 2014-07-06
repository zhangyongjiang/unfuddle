package com.gaoshin.cloud.web.xen.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.bean.BusinessException;
import com.gaoshin.cloud.web.bean.ServiceError;
import com.gaoshin.cloud.web.xen.bean.CloneRequest;
import com.gaoshin.cloud.web.xen.bean.ConsoleSession;
import com.gaoshin.cloud.web.xen.bean.Host;
import com.gaoshin.cloud.web.xen.bean.HostDetails;
import com.gaoshin.cloud.web.xen.bean.HostList;
import com.gaoshin.cloud.web.xen.bean.HostNetworkList;
import com.gaoshin.cloud.web.xen.bean.SnapshotRequest;
import com.gaoshin.cloud.web.xen.bean.StorageRepo;
import com.gaoshin.cloud.web.xen.bean.StorageRepoDetails;
import com.gaoshin.cloud.web.xen.bean.StorageRepoList;
import com.gaoshin.cloud.web.xen.bean.VirtualDiskImage;
import com.gaoshin.cloud.web.xen.bean.VirtualDiskImageList;
import com.gaoshin.cloud.web.xen.bean.VmDetails;
import com.gaoshin.cloud.web.xen.bean.VmList;
import com.gaoshin.cloud.web.xen.bean.XenConsole;
import com.gaoshin.cloud.web.xen.bean.XenSession;
import com.gaoshin.cloud.web.xen.dao.XenDao;
import com.gaoshin.cloud.web.xen.entity.HostEntity;
import com.gaoshin.cloud.web.xen.entity.HostStatus;
import com.gaoshin.cloud.web.xen.entity.StorageRepoEntity;
import com.gaoshin.cloud.web.xen.entity.VirtualDiskImageEntity;
import com.xensource.xenapi.VM;
import common.util.reflection.ReflectionUtil;

@Service("xenService")
@Transactional
public class XenServiceImpl implements XenService {
    private static final Logger logger = Logger.getLogger(XenServiceImpl.class);
    
    @Autowired
    private XenDao xenDao;
    
    private Map<String, XenSession> sessionPool = new HashMap<String, XenSession>();
    
    @PostConstruct
    public void houseKeeping() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(10000);
                        synchronized (XenServiceImpl.this) {
                            XenSession[] sessions = sessionPool.values().toArray(new XenSession[0]);
                            for(XenSession session : sessions) {
                                long diff = System.currentTimeMillis() - session.getLastAccessTime();
                                if(diff>30000) {
                                    System.out.println("========= Remove inactive session " + session.getSessionReference());
                                    sessionPool.remove(session.getSessionReference());
                                    session.close();
                                }
                            }
                        }
                    }
                    catch (InterruptedException e) {
                        logger.error("", e);
                    }
                }
            }}
        ).start();
    }

    @Override
    public Host createHost(Host host) {
        try {
            HostMetricsXenTask hostMetricsXenTask = new HostMetricsXenTask(host.getUrl(), host.getUser(), host.getPassword());
            hostMetricsXenTask.exec();
            host.setCpus(hostMetricsXenTask.getCpus());
            host.setMemoryFree(hostMetricsXenTask.getMemoryFree());
            host.setMemoryTotal(hostMetricsXenTask.getMemoryTotal());
            host.setStatus(HostStatus.Verified);
        }
        catch (Exception e) {
            host.setStatus(HostStatus.Disconnected);
        }
        HostEntity entity = ReflectionUtil.copy(HostEntity.class, host);
        xenDao.saveEntity(entity);
        host.setId(entity.getId());

        if(HostStatus.Verified.equals(host.getStatus())) {
            try {
                refreshHostStorageRepository(host);
            }
            catch (Exception e) {
                logger.error(null, e);
            }
            
            try {
                refreshHostVirtualDiskImages(host);
            }
            catch (Exception e) {
                logger.error(null, e);
            }
        }
        
        return host;
    }

    @Override
    public HostList listHosts() {
        HostList list = new HostList();
        List<HostEntity> entities = xenDao.listHosts();
        for(HostEntity he : entities) {
            Host host = ReflectionUtil.copy(Host.class, he);
            list.getItems().add(host);
        }
        list.sumup();
        return list;
    }

    @Override
    public Host getHost(Long hostId) {
        HostEntity entity = xenDao.getEntity(HostEntity.class, hostId);
        return ReflectionUtil.copy(Host.class, entity);
    }

    @Override
    public HostDetails getHostDetails(Long hostId) {
        HostEntity entity = xenDao.getEntity(HostEntity.class, hostId);
        if(entity.getMemoryFree() == 0) {
            HostMetricsXenTask hostMetricsXenTask = new HostMetricsXenTask(entity.getUrl(), entity.getUser(), entity.getPassword());
            try {
                hostMetricsXenTask.exec();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            entity.setCpus(hostMetricsXenTask.getCpus());
            entity.setMemoryFree(hostMetricsXenTask.getMemoryFree());
            entity.setMemoryTotal(hostMetricsXenTask.getMemoryTotal());
            xenDao.merge(entity);
        }
        final HostDetails details = ReflectionUtil.copy(HostDetails.class, entity);
        
        try {
            details.setVmList(listHostVms(hostId));
            if(!HostStatus.Verified.equals(entity.getStatus())) {
                entity.setStatus(HostStatus.Verified);
                xenDao.merge(entity);
                details.setStatus(HostStatus.Verified);
            }
        }
        catch (Exception e) {
            entity.setStatus(HostStatus.Disconnected);
            xenDao.merge(entity);
            details.setStatus(HostStatus.Disconnected);
        }
            
        {
            List<StorageRepoEntity> entities = xenDao.listHostStorageRepos(hostId);
            if(entities.size() == 0) {
                try {
                    refreshHostStorageRepository(details);
                    entities = xenDao.listHostStorageRepos(hostId);
                }
                catch (Exception e) {
                    logger.error("", e);
                }
            }
            for(StorageRepoEntity sre : entities) {
                StorageRepo sr = ReflectionUtil.copy(StorageRepo.class, sre);
                details.getStorageRepoList().getItems().add(sr);
            }
            details.getStorageRepoList().sumup();
        }
        
        {
            List<VirtualDiskImageEntity> entities = xenDao.listHostVirtualDiskImages(hostId);
            if(entities.size() == 0) {
                try {
                    refreshHostVirtualDiskImages(details);
                    entities = xenDao.listHostVirtualDiskImages(hostId);
                }
                catch (Exception e) {
                    logger.error("", e);
                }
            }
            for(VirtualDiskImageEntity sre : entities) {
                VirtualDiskImage sr = ReflectionUtil.copy(VirtualDiskImage.class, sre);
                details.getVdiList().getItems().add(sr);
            }
            Collections.sort(details.getVdiList().getItems(), new Comparator<VirtualDiskImage>() {
                @Override
                public int compare(VirtualDiskImage o1, VirtualDiskImage o2) {
                    return o1.getNameLabel().compareToIgnoreCase(o2.getNameLabel());
                }
            });
        }
        
        return details;
    }

    @Override
    public void updateHost(Host host) {
        HostEntity entity = ReflectionUtil.copy(HostEntity.class, host);
        xenDao.merge(entity);
    }

    @Override
    public void removeHost(Long hostId) {
        HostEntity entity = xenDao.getEntity(HostEntity.class, hostId);
        xenDao.deleteEntity(entity);
        xenDao.removeHostStorageRepositoryList(hostId);
    }

    @Override
    public VmList listHostVms(Long hostId) throws Exception {
        VmList list = new VmList();
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        if(!HostStatus.Verified.equals(host.getStatus())) {
            return list;
        }
        FindVmsXenTask findVmsXenTask = new FindVmsXenTask(host.getUrl(), host.getUser(), host.getPassword());
        findVmsXenTask.setHostId(hostId);
        findVmsXenTask.exec();
        return findVmsXenTask.getVmList();
    }
    
    @Override
    public void startVm(Long hostId, final String vmId) {
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            new XenTask(host.getUrl(), host.getUser(), host.getPassword()) {
                @Override
                protected void doTask() throws Exception {
                    VM vm = VM.getByUuid(connection, vmId);
                    vm.start(connection, false, false);
                }
            }.exec();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }

    @Override
    public void shutdownVm(final Long hostId, final String vmId) {
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            new XenTask(host.getUrl(), host.getUser(), host.getPassword()) {
                @Override
                protected void doTask() throws Exception {
                    VM vm = VM.getByUuid(connection, vmId);
                    vm.cleanShutdown(connection);
                }
            }.exec();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }

    @Override
    public void suspendVm(Long hostId, final String vmId) {
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            new XenTask(host.getUrl(), host.getUser(), host.getPassword()) {
                @Override
                protected void doTask() throws Exception {
                    VM vm = VM.getByUuid(connection, vmId);
                    vm.suspend(connection);
                }
            }.exec();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }

    @Override
    public void resumeVm(Long hostId, final String vmId) {
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            new XenTask(host.getUrl(), host.getUser(), host.getPassword()) {
                @Override
                protected void doTask() throws Exception {
                    VM vm = VM.getByUuid(connection, vmId);
                    vm.resume(connection, false, false);
                }
            }.exec();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }

    @Override
    public VmDetails getVmDetails(final Long hostId, final String vmId) {
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            VmDetailsXenTask vmDetailsXenTask = new VmDetailsXenTask(host.getUrl(), host.getUser(), host.getPassword(), hostId, vmId);
            vmDetailsXenTask.exec();
            VmDetails vmDetails = vmDetailsXenTask.getVmDetails();
            vmDetails.setHost(ReflectionUtil.copy(Host.class, host));
            return vmDetails;
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }
    
    @Override
    public void destroyVm(Long hostId, final String vmId) {
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            new XenTask(host.getUrl(), host.getUser(), host.getPassword()) {
                @Override
                protected void doTask() throws Exception {
                    VM vm = VM.getByUuid(connection, vmId);
                    vm.destroy(connection);
                }
            }.exec();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }
    
    @Override
    public ConsoleSession getConsole(Long hostId, String vmId, String consoleId) throws Exception {
        Host host = getHost(hostId);
        XenSession xenSession = new XenSession(host);
        xenSession.open();
        VmDetails details = VmDetailsXenTask.doTask(hostId, xenSession.getConnection(), vmId);
        ConsoleSession cs = new ConsoleSession();
        sessionPool.put(xenSession.getSessionReference(), xenSession);
        cs.setSessionId(xenSession.getSessionReference());
        if(details.getDomid() != null && details.getDomid() > 0) {
            for(XenConsole console : details.getConsoleList()) {
                if(console.getUuid().equals(consoleId)) {
                    cs.setConsole(console );
                }
            }
        }
        return cs;
    }

    @Override
    public void sessionHeartBeat(String sessionId) {
        XenSession session = sessionPool.get(sessionId);
        if(session == null) {
            return;
        }
        session.setLastAccessTime(System.currentTimeMillis());
    }

    @Override
    public void refreshHostStorageRepository(Long hostId) throws Exception {
        Host host = getHost(hostId);
        refreshHostStorageRepository(host);
    }

    public void refreshHostStorageRepository(Host host) throws Exception {
        Long hostId = host.getId();
        xenDao.removeHostStorageRepositoryList(hostId);
        StorageRepoXenTask storageRepoXenTask = new StorageRepoXenTask(host.getUrl(), host.getUser(), host.getPassword(), host.getId());
        storageRepoXenTask.exec();
        for(StorageRepo sr : storageRepoXenTask.getRepos().getItems()) {
            StorageRepoEntity srEntity = ReflectionUtil.copy(StorageRepoEntity.class, sr);
            srEntity.setHostId(hostId);
            xenDao.merge(srEntity);
        }
    }

    @Override
    public void refreshHostVirtualDiskImages(Long hostId) throws Exception {
        Host host = getHost(hostId);
        refreshHostVirtualDiskImages(host);
    }

    public void refreshHostVirtualDiskImages(Host host) throws Exception {
        Long hostId = host.getId();
        xenDao.removeHostVirtualDiskImageList(hostId);
        VirtualDiskImageXenTask xenTask = new VirtualDiskImageXenTask(host.getUrl(), host.getUser(), host.getPassword(), host.getId());
        xenTask.exec();
        for(VirtualDiskImage bean : xenTask.getItems()) {
            VirtualDiskImageEntity entity = ReflectionUtil.copy(VirtualDiskImageEntity.class, bean);
            xenDao.merge(entity);
        }
    }

    @Override
    public StorageRepoDetails getStorageRepoDetails(Long hostId, String srUuid) throws Exception {
        StorageRepoEntity entity = xenDao.getStorageRepoByUuid(srUuid);
        StorageRepoDetails details = ReflectionUtil.copy(StorageRepoDetails.class, entity);
        Host host = getHost(hostId);
        details.setHost(host);
        StorageRepoVirtualDiskImageXenTask xenTask = new StorageRepoVirtualDiskImageXenTask(host.getUrl(), host.getUser(), host.getPassword(), host.getId(), srUuid);
        xenTask.exec();
        details.getVdiList().setItems(xenTask.getItems());
        return details;
    }

    @Override
    public VirtualDiskImageList listHostVdis(Long hostId) throws Exception {
        Host host = getHost(hostId);
        VirtualDiskImageXenTask xenTask = new VirtualDiskImageXenTask(host.getUrl(), host.getUser(), host.getPassword(), host.getId());
        xenTask.exec();
        VirtualDiskImageList list = new VirtualDiskImageList();
        list.setItems(xenTask.getItems());
        return list;
    }

    @Override
    public StorageRepoList listHostStorage(Long hostId) throws Exception {
        Host host = getHost(hostId);
        StorageRepoXenTask storageRepoXenTask = new StorageRepoXenTask(host.getUrl(), host.getUser(), host.getPassword(), host.getId());
        storageRepoXenTask.exec();
        return storageRepoXenTask.getRepos();
    }

    @Override
    public HostNetworkList listNetwork(Long hostId) throws Exception {
        Host host = getHost(hostId);
        NetworkXenTask xenTask = new NetworkXenTask(host.getUrl(), host.getUser(), host.getPassword(), host.getId());
        xenTask.exec();
        return xenTask.getNetworks();
    }

    @Override
    public String cloneVm(CloneRequest cloneRequest) {
        Long hostId = cloneRequest.getHostId();
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            CloneVmXenTask task = new CloneVmXenTask(host.getUrl(), host.getUser(), host.getPassword(), cloneRequest);
            task.exec();
            return task.getNewVmUuid();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }

    @Override
    public String snapshotVm(SnapshotRequest request) {
        Long hostId = request.getHostId();
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            SnapshotVmXenTask task = new SnapshotVmXenTask(host.getUrl(), host.getUser(), host.getPassword(), request);
            task.exec();
            return task.getNewVmUuid();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }

    @Override
    public void changeNetwork(Long hostId, String vmId) {
        HostEntity host = xenDao.getEntity(HostEntity.class, hostId);
        try {
            ChangeVmNetworkXenTask task = new ChangeVmNetworkXenTask(host.getUrl(), host.getUser(), host.getPassword(), hostId, vmId);
            task.exec();
        }
        catch (BusinessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e);
        }
    }
}
