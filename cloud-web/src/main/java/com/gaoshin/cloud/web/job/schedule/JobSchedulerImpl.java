package com.gaoshin.cloud.web.job.schedule;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.job.bean.JobConf;
import com.gaoshin.cloud.web.job.bean.JobConfKey;
import com.gaoshin.cloud.web.job.bean.JobConfList;
import com.gaoshin.cloud.web.job.bean.JobExecution;
import com.gaoshin.cloud.web.job.bean.WorkStatus;
import com.gaoshin.cloud.web.job.dao.JobDao;
import com.gaoshin.cloud.web.job.entity.JobConfEntity;
import com.gaoshin.cloud.web.job.entity.JobEntity;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.RuntimeJobConfEntity;
import common.util.reflection.ReflectionUtil;

@Service("jobScheduler")
@Transactional
public class JobSchedulerImpl implements JobScheduler {
    @Autowired private JobDao jobDao;
    @Autowired private JobExecutionManager jobExecutionManager;

    public JobSchedulerImpl() {
        startCronSchedulerThread();
    }
    
    private void startCronSchedulerThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(jobDao == null || jobExecutionManager == null) {
                    try {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e) {
                    }
                }
                while(true) {
                    schedule();
                    try {
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }
    
    private void schedule() {
        checkAllCronJobs();
        jobExecutionManager.checkDueJob();
    }

    protected void checkAllCronJobs() {
        List<JobEntity> jobs = jobDao.getEnabledCronJobs();
        for(JobEntity je : jobs) {
            checkCronJob(je);
        }
    }

    private void checkCronJob(JobEntity je) {
        JobExecutionEntity jee = jobDao.getLastCronScheduledJobExecution(je.getId());
        long lastScheduleTime = System.currentTimeMillis();
        if(jee != null) {
            lastScheduleTime = jee.getScheduledStartTime();
        }
        if(lastScheduleTime > System.currentTimeMillis()) {
            return;
        }
        
        try {
            CronExpression cr = new CronExpression(je.getCronExpression());
            long scheduledTime = lastScheduleTime;
            while(true) {
                Date next = cr.getNextValidTimeAfter(new Date(scheduledTime));
                scheduledTime = next.getTime();
                if(scheduledTime > System.currentTimeMillis()) {
                    break;
                }
            }
            JobConfList confList = new JobConfList();
            confList.getList().add(new JobConf(JobConfKey.Timestamp.name(), String.valueOf(scheduledTime)));
            runJob(je.getId(), confList);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enableJob(Long jobId, boolean enable) {
        if(enable) {
            schedule();
        }
    }

    @Override
    public JobExecution runJob(Long jobId, JobConfList confList) {
        JobEntity jobEntity = jobDao.getEntity(JobEntity.class, jobId);
        JobExecutionEntity jee = ReflectionUtil.copy(JobExecutionEntity.class, jobEntity);
        jee.setId(null);
        jee.setJobId(jobId);
        jee.setStatus(WorkStatus.Pending);
        jee.setNote(jobEntity.getDescription());
        
        JobConf timeConf = confList.search(JobConfKey.Timestamp.name());
        long startTime = System.currentTimeMillis() + 5000;
        if(timeConf != null) {
            startTime = Long.parseLong(timeConf.getCvalue());
        }
        else {
            timeConf = new JobConf(JobConfKey.Timestamp.name(), String.valueOf(startTime));
        }
        jee.setStartTime(startTime);
        jee.setScheduledStartTime(startTime);
        
        jobDao.saveEntity(jee);
        
        Map<String, String> jobConfs = getDefaultJobConfs(jobEntity);
        jobConfs.put(timeConf.getCkey(), timeConf.getCvalue());
        jobConfs.put(JobConfKey.JobId.name(), String.valueOf(jobId));
        jobConfs.put(JobConfKey.JobExecutionId.name(), String.valueOf(jee.getId()));
        for(JobConf jc : confList.getList()) {
            jobConfs.put(jc.getCkey(), jc.getCvalue());
        }
        for(Entry<String, String> entry : jobConfs.entrySet()) {
            RuntimeJobConfEntity entity = new RuntimeJobConfEntity(entry.getKey(), entry.getValue());
            entity.setJobExecutionId(jee.getId());
            jobDao.saveEntity(entity);
        }
        
        jobExecutionManager.checkDueJob();
        
        JobExecution je = ReflectionUtil.copy(JobExecution.class, jee);
        return je;
    }
    
    private Map<String, String> getDefaultJobConfs(JobEntity jobEntity) {
        Map<String, String> confMap = new HashMap<String, String>();

        // global conf
        List<JobConfEntity> confList = jobDao.getJobConfList(0l);
        for(JobConfEntity jce : confList) {
            confMap.put(jce.getCkey(), jce.getCvalue());
        }
        
        confList = jobDao.getJobConfList(jobEntity.getId());
        for(JobConfEntity jce : confList) {
            confMap.put(jce.getCkey(), jce.getCvalue());
        }
        
        return confMap;
    }
}
