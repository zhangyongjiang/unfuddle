package com.gaoshin.cloud.web.xen.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.cloud.web.dao.HibernateBaseDao;
import com.gaoshin.cloud.web.xen.entity.HostEntity;
import com.gaoshin.cloud.web.xen.entity.StorageRepoEntity;
import com.gaoshin.cloud.web.xen.entity.VirtualDiskImageEntity;

@Repository("xenDao")
public class XenDaoImpl extends HibernateBaseDao implements XenDao {

    @Override
    public List<HostEntity> listHosts() {
        return find(HostEntity.class, "from HostEntity");
    }

    @Override
    public List<StorageRepoEntity> listHostStorageRepos(Long hostId) {
        return find(StorageRepoEntity.class, "from StorageRepoEntity sre where sre.hostId=?", hostId);
    }

    @Override
    public void removeHostStorageRepositoryList(Long hostId) {
        nativeUpdate("delete from StorageRepoEntity where hostId=" + hostId);
    }

    @Override
    public void removeHostVirtualDiskImageList(Long hostId) {
        nativeUpdate("delete from VirtualDiskImageEntity where hostId=" + hostId);
    }

    @Override
    public List<VirtualDiskImageEntity> listHostVirtualDiskImages(Long hostId) {
        return find(VirtualDiskImageEntity.class, "from VirtualDiskImageEntity sre where sre.hostId=?", hostId);
    }

    @Override
    public StorageRepoEntity getStorageRepoByUuid(String srUuid) {
        List<StorageRepoEntity> list = find(StorageRepoEntity.class, "from StorageRepoEntity sre where sre.uuid=?", srUuid);
        return list.isEmpty() ? null : list.get(0);
    }

}