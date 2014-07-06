package com.gaoshin.cloud.web.job.bean;

public class JobDependencyDetails extends JobDependency {
    private Job job;
    private Job blockedByJob;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Job getBlockedByJob() {
        return blockedByJob;
    }

    public void setBlockedByJob(Job blockedByJob) {
        this.blockedByJob = blockedByJob;
    }
}
