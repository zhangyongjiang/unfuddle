package com.gaoshin.cloud.web.vm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.cloud.web.dao.HibernateBaseDao;
import com.gaoshin.cloud.web.vm.entity.ClusterEntity;
import com.gaoshin.cloud.web.vm.entity.DataCenterEntity;
import com.gaoshin.cloud.web.vm.entity.PodEntity;

@Repository("dataCenterDao")
public class DataCenterDaoImpl extends HibernateBaseDao implements DataCenterDao {

    @Override
    public List<PodEntity> listPods(Long dataCenterId) {
        return find("from PodEntity jde where jde.dataCenterId=?", dataCenterId);
    }

    @Override
    public List<DataCenterEntity> listDataCenterEntitys() {
        return find(DataCenterEntity.class, "from DataCenterEntity jde");
    }

    @Override
    public List<ClusterEntity> listClusters(Long podId) {
        return find("from ClusterEntity jde where jde.podId=?", podId);
    }

}