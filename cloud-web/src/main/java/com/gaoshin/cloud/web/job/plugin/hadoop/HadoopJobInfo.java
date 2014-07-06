package com.gaoshin.cloud.web.job.plugin.hadoop;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HadoopJobInfo {
    private String jobId;
    private String jobName;
    private String jobConfUrl;
    private String trackingUrl;
    private float mapProgress;
    private float reduceProgress;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobConfUrl() {
        return jobConfUrl;
    }

    public void setJobConfUrl(String jobConfUrl) {
        this.jobConfUrl = jobConfUrl;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public float getMapProgress() {
        return mapProgress;
    }

    public void setMapProgress(float mapProgress) {
        this.mapProgress = mapProgress;
    }

    public float getReduceProgress() {
        return reduceProgress;
    }

    public void setReduceProgress(float reduceProgress) {
        this.reduceProgress = reduceProgress;
    }
}
