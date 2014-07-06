package com.gaoshin.cloud.web.xen.dao;

import java.util.List;

import com.gaoshin.cloud.web.dao.GenericDao;
import com.gaoshin.cloud.web.xen.entity.HostEntity;
import com.gaoshin.cloud.web.xen.entity.StorageRepoEntity;
import com.gaoshin.cloud.web.xen.entity.VirtualDiskImageEntity;

public interface XenDao extends GenericDao {

    List<HostEntity> listHosts();

    List<StorageRepoEntity> listHostStorageRepos(Long hostId);

    void removeHostStorageRepositoryList(Long hostId);

    void removeHostVirtualDiskImageList(Long hostId);

    List<VirtualDiskImageEntity> listHostVirtualDiskImages(Long hostId);

    StorageRepoEntity getStorageRepoByUuid(String srUuid);

}
