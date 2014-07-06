package com.gaoshin.cloud.web.vm.dao;

import java.util.List;

import com.gaoshin.cloud.web.dao.GenericDao;
import com.gaoshin.cloud.web.vm.entity.ClusterEntity;
import com.gaoshin.cloud.web.vm.entity.DataCenterEntity;
import com.gaoshin.cloud.web.vm.entity.PodEntity;

public interface DataCenterDao extends GenericDao {

    List<PodEntity> listPods(Long dataCenterId);

    List<DataCenterEntity> listDataCenterEntitys();

    List<ClusterEntity> listClusters(Long podId);

}
