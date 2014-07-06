package com.gaoshin.cloud.web.job.dao;

import java.util.List;

import com.gaoshin.cloud.web.dao.GenericDao;
import com.gaoshin.cloud.web.job.bean.WorkStatus;
import com.gaoshin.cloud.web.job.entity.JobConfEntity;
import com.gaoshin.cloud.web.job.entity.JobDependencyEntity;
import com.gaoshin.cloud.web.job.entity.JobEntity;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.RuntimeJobConfEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;

public interface JobDao extends GenericDao {

    List<JobDependencyEntity> getDownstreamJobs(Long jobId);

    List<JobExecutionEntity> getDueJobExecutions();

    List<JobEntity> getUpstreams(Long jobId);

    List<JobDependencyEntity> getDependencies(Long jobId);

    List<JobExecutionEntity> getJobExecution(Long upstreamJobId, long upstreamStartTime, WorkStatus succeed);

    List<TaskEntity> getOrderedJobTasks(Long jobId);

    List<JobConfEntity> getJobConfList(Long jobId);

    List<JobExecutionEntity> getJobExecutionList(Long jobId);

    List<TaskExecutionEntity> getJobExecutionChildren(Long jobExecutionId);

    List<JobEntity> getEnabledCronJobs();

    JobExecutionEntity getLastCronScheduledJobExecution(Long jobId);

    RuntimeJobConfEntity getJobExecutionConf(Long jobExecutionId, String key);

    List<RuntimeJobConfEntity> getJobExecutionConfList(Long jobExecutionId);
}
