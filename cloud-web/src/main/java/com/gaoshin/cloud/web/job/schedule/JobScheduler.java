package com.gaoshin.cloud.web.job.schedule;

import com.gaoshin.cloud.web.job.bean.JobConfList;
import com.gaoshin.cloud.web.job.bean.JobExecution;

public interface JobScheduler {

    void enableJob(Long jobId, boolean enable);

    JobExecution runJob(Long jobId, JobConfList confList);

}
