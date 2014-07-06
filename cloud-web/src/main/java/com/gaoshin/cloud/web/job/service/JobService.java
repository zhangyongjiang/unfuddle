package com.gaoshin.cloud.web.job.service;

import com.gaoshin.cloud.web.job.bean.Job;
import com.gaoshin.cloud.web.job.bean.JobConf;
import com.gaoshin.cloud.web.job.bean.JobConfDetails;
import com.gaoshin.cloud.web.job.bean.JobDependency;
import com.gaoshin.cloud.web.job.bean.JobDependencyDetails;
import com.gaoshin.cloud.web.job.bean.JobDetails;
import com.gaoshin.cloud.web.job.bean.JobList;
import com.gaoshin.cloud.web.job.bean.Task;
import com.gaoshin.cloud.web.job.bean.TaskDetails;

public interface JobService {

    Job create(Job job);

    JobList list();

    void delete(Long jobId);

    JobDetails getJobDetails(Long jobId);

    JobConf createJobConf(JobConf jobConf);

    JobConfDetails getJobConfDetails(Long jobConfId);

    void updateJobConfDetails(JobConf jobConf);

    void deleteJobConf(Long jobConfId);

    Task createTask(Task task);

    TaskDetails getTaskDetails(Long taskId);

    void updateTaskDetails(Task task);

    void deleteTask(Long taskId);

    JobDependency createJobDependency(JobDependency jobDependency);

    JobDependencyDetails getJobDependencyDetails(Long jobDependencyId);

    void updateJobDependencyDetails(JobDependency jobDependency);

    void deleteJobDependency(Long jobDependencyId);

    void enableJob(Long jobId, boolean enable);

    Job update(Job job);
}
