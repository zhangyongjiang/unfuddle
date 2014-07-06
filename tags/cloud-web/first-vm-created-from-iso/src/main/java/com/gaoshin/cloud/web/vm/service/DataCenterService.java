package com.gaoshin.cloud.web.vm.service;

import com.gaoshin.cloud.web.vm.bean.DataCenter;
import com.gaoshin.cloud.web.vm.bean.DataCenterDetails;
import com.gaoshin.cloud.web.vm.bean.DataCenterList;
import com.gaoshin.cloud.web.vm.bean.Pod;
import com.gaoshin.cloud.web.vm.bean.PodDetails;

public interface DataCenterService {

    DataCenter createDataCenter(DataCenter dataCenter);

    DataCenterDetails getDataCenterDetails(Long dataCenterId);

    DataCenterList listDataCenters();

    DataCenter updateDataCenter(DataCenter dataCenter);

    void deleteDataCenter(Long dataCenterId);

    Pod createPod(Pod pod);

    Pod updatePod(Pod pod);

    void deletePod(Long podId);

    PodDetails getPodDetails(Long podId);

}
