package com.gaoshin.cloud.web.job.bean;

import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;

public interface TaskProcessor {
    String getName();
    void run(JobExecutionEntity jee, TaskEntity toBeExecuted);
}
