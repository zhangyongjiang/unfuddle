package com.gaoshin.cloud.web.vm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.vm.bean.Cluster;
import com.gaoshin.cloud.web.vm.bean.DataCenter;
import com.gaoshin.cloud.web.vm.bean.DataCenterDetails;
import com.gaoshin.cloud.web.vm.bean.DataCenterList;
import com.gaoshin.cloud.web.vm.bean.Pod;
import com.gaoshin.cloud.web.vm.bean.PodDetails;
import com.gaoshin.cloud.web.vm.dao.DataCenterDao;
import com.gaoshin.cloud.web.vm.entity.ClusterEntity;
import com.gaoshin.cloud.web.vm.entity.DataCenterEntity;
import com.gaoshin.cloud.web.vm.entity.PodEntity;
import common.util.reflection.ReflectionUtil;

@Service("dataCenterService")
@Transactional
public class DataCenterServiceImpl implements DataCenterService {
    @Autowired
    private DataCenterDao dataCenterDao;

    @Override
    public DataCenter createDataCenter(DataCenter dataCenter) {
        DataCenterEntity entity = ReflectionUtil.copy(DataCenterEntity.class, dataCenter);
        if(entity.getRouterMacAddress() == null) {
            entity.setRouterMacAddress("02:00:00:00:00:01");
        }
        dataCenterDao.saveEntity(entity);
        dataCenter.setId(entity.getId());
        return dataCenter;
    }

    @Override
    public DataCenterDetails getDataCenterDetails(Long dataCenterId) {
        DataCenterEntity entity = dataCenterDao.getEntity(DataCenterEntity.class, dataCenterId);
        DataCenterDetails bean = ReflectionUtil.copy(DataCenterDetails.class, entity);
        
        List<PodEntity> entities = dataCenterDao.listPods(dataCenterId);
        for(PodEntity pe : entities) {
            Pod pod = ReflectionUtil.copy(Pod.class, pe);
            bean.getPodList().add(pod);
        }
        
        return bean;
    }

    @Override
    public DataCenterList listDataCenters() {
        DataCenterList list = new DataCenterList();
        List<DataCenterEntity> entities = dataCenterDao.listDataCenterEntitys();
        for(DataCenterEntity pe : entities) {
            DataCenter bean = ReflectionUtil.copy(DataCenter.class, pe);
            list.getItems().add(bean);
        }
        return list;
    }

    @Override
    public DataCenter updateDataCenter(DataCenter dataCenter) {
        DataCenterEntity entity = ReflectionUtil.copy(DataCenterEntity.class, dataCenter);
        if(entity.getRouterMacAddress() == null) {
            entity.setRouterMacAddress("02:00:00:00:00:01");
        }
        dataCenterDao.merge(entity);
        return dataCenter;
    }

    @Override
    public void deleteDataCenter(Long dataCenterId) {
        DataCenterEntity entity = dataCenterDao.getEntity(DataCenterEntity.class, dataCenterId);
        dataCenterDao.deleteEntity(entity);
    }

    @Override
    public Pod createPod(Pod pod) {
        PodEntity entity = ReflectionUtil.copy(PodEntity.class, pod);
        dataCenterDao.saveEntity(entity);
        pod.setId(entity.getId());
        return pod;
    }

    @Override
    public Pod updatePod(Pod pod) {
        PodEntity entity = ReflectionUtil.copy(PodEntity.class, pod);
        dataCenterDao.merge(entity);
        return pod;
    }

    @Override
    public void deletePod(Long podId) {
        PodEntity entity = dataCenterDao.getEntity(PodEntity.class, podId);
        dataCenterDao.deleteEntity(entity);
    }

    @Override
    public PodDetails getPodDetails(Long podId) {
        PodEntity entity = dataCenterDao.getEntity(PodEntity.class, podId);
        PodDetails details = ReflectionUtil.copy(PodDetails.class, entity);
        
        DataCenterEntity dce = dataCenterDao.getEntity(DataCenterEntity.class, entity.getDataCenterId());
        DataCenter dc = ReflectionUtil.copy(DataCenter.class, dce);
        details.setDataCenter(dc);

        List<ClusterEntity> entities = dataCenterDao.listClusters(podId);
        for(ClusterEntity ce : entities) {
            Cluster c = ReflectionUtil.copy(Cluster.class, ce);
            details.getClusterList().getItems().add(c);
        }
        
        return details;
    }

}
