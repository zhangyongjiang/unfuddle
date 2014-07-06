package com.gaoshin.cloud.web.job.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.bean.StringList;
import com.gaoshin.cloud.web.job.bean.Job;
import com.gaoshin.cloud.web.job.bean.JobExecutionDetails;
import com.gaoshin.cloud.web.job.bean.JobExecutionDetailsList;
import com.gaoshin.cloud.web.job.bean.RuntimeJobConf;
import com.gaoshin.cloud.web.job.bean.TaskExecution;
import com.gaoshin.cloud.web.job.bean.TaskProcessor;
import com.gaoshin.cloud.web.job.bean.WorkStatus;
import com.gaoshin.cloud.web.job.dao.JobDao;
import com.gaoshin.cloud.web.job.entity.JobDependencyEntity;
import com.gaoshin.cloud.web.job.entity.JobEntity;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.Parameter;
import com.gaoshin.cloud.web.job.entity.RuntimeJobConfEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;
import common.util.reflection.ReflectionUtil;

@Service("jobExecutionManager")
@Transactional
public class JobExecutionManagerImpl implements JobExecutionManager, ApplicationContextAware {
    @Autowired
    private JobDao jobDao;
    
    private ApplicationContext springContext;

    @Override
    public void setApplicationContext(ApplicationContext springContext) throws BeansException {
        this.springContext = springContext;
    }
    
    private TaskProcessor getTaskProcessor(String name) {
        Map<String, TaskProcessor> beans = springContext.getBeansOfType(TaskProcessor.class);
        for(TaskProcessor bean : beans.values()) {
            if(bean.getName().equals(name)) {
                return bean;
            }
        }
        return null;
    }
    
    @Override
    public void checkDueJob() {
        List<JobExecutionEntity> dueJobList = jobDao.getDueJobExecutions();
        for(JobExecutionEntity jee : dueJobList) {
            if(!areUpstreamsReady(jee)) {
                continue;
            }
            runJobExecution(jee);
        }
    }

    private void runJobExecution(JobExecutionEntity jee) {
        List<TaskEntity> tasks = jobDao.getOrderedJobTasks(jee.getJobId());
        if(tasks.size() == 0) {
            jee.setStatus(WorkStatus.Succeed);
            jee.setStartTime(System.currentTimeMillis());
            jee.setDuration(0l);
            jobDao.saveEntity(jee);
            return;
        }
        
        TaskEntity toBeExecuted = null;
        for(TaskEntity te : tasks) {
            if(!te.isDisabled() && !te.isErrorHandlingTask()) {
                toBeExecuted = te;
                break;
            }
        }
        
        if(toBeExecuted == null) {
            jee.setStatus(WorkStatus.Succeed);
            jee.setStartTime(System.currentTimeMillis());
            jee.setDuration(0l);
            jobDao.saveEntity(jee);
            return;
        }
        
        jee.setStatus(WorkStatus.Running);
        jee.setStartTime(System.currentTimeMillis());
        jobDao.saveEntity(jee);
        execTask(jee, toBeExecuted);
    }

    private void execTask(JobExecutionEntity jee, TaskEntity toBeExecuted) {
        try {
            String handler = toBeExecuted.getHandler();
            TaskProcessor taskProcessor = getTaskProcessor(handler);
            taskProcessor.run(jee, toBeExecuted);
        }
        catch (Exception e) {
            jee.setStatus(WorkStatus.Failed);
            jee.appendNote(e);
            jobDao.saveEntity(jee);
        }
    }

    private boolean areUpstreamsReady(JobExecutionEntity jee) {
        Long jobId = jee.getJobId();
        long scheduledStartTime = jee.getScheduledStartTime();
        List<JobDependencyEntity> upstreamDependencies = jobDao.getDependencies(jobId);
        for(JobDependencyEntity jde : upstreamDependencies) {
            long timeDiff = jde.getTimeDiff();
            long upstreamStartTime = scheduledStartTime + timeDiff;
            List<JobExecutionEntity> jeList = jobDao.getJobExecution(jde.getUpstreamJobId(), upstreamStartTime, WorkStatus.Succeed);
            if(jeList.size() == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public StringList listTaskType() {
        Map<String, TaskProcessor> beansOfType = springContext.getBeansOfType(TaskProcessor.class);
        StringList ret = new StringList();
        for(TaskProcessor tp : beansOfType.values()) {
            ret.getList().add(tp.getName());
        }
        return ret;
    }

    @Override
    public void taskExecutionSucceed(Long taskExecutionEntityId) {
        TaskExecutionEntity tee = jobDao.getEntity(TaskExecutionEntity.class, taskExecutionEntityId);
        tee.setDuration((System.currentTimeMillis() - tee.getStartTime()));
        tee.setStatus(WorkStatus.Succeed);
        jobDao.merge(tee);
     
        TaskEntity next = getNextTask(tee.getJobExecutionId(), tee.getExecOrder(), tee.getNextTaskExecOrder());
        if(next == null) {
            jobSucceed(tee.getJobExecutionId());
        }
        else {
            JobExecutionEntity jee = jobDao.getEntity(JobExecutionEntity.class, tee.getJobExecutionId());
            execTask(jee, next);
        }
    }

    private TaskEntity getNextTask(Long jobExecutionId, int execOrder, Integer nextTaskExecOrder) {
        JobExecutionEntity jee = jobDao.getEntity(JobExecutionEntity.class, jobExecutionId);
        JobEntity je = jobDao.getEntity(JobEntity.class, jee.getJobId());
        List<TaskEntity> tasks = jobDao.getOrderedJobTasks(je.getId());
        if(tasks.isEmpty()) {
            return null;
        }
        
        if(nextTaskExecOrder != null) {
            for(TaskEntity te : tasks) {
                if(te.getExecOrder() == nextTaskExecOrder) {
                    return te;
                }
            }
            return tasks.get(tasks.size() - 1);
        }
        
        for(int i=0; i<tasks.size(); i++) {
            TaskEntity te = tasks.get(i);
            if(te.getExecOrder() == execOrder) {
                return (i == (tasks.size()-1)) ? null : tasks.get(i+1);
            }
        }
        
        return null;
    }

    @Override
    public void taskExecutionFailed(Long taskExecutionEntityId) {
        TaskExecutionEntity tee = jobDao.getEntity(TaskExecutionEntity.class, taskExecutionEntityId);
        tee.setDuration((System.currentTimeMillis() - tee.getStartTime()));
        tee.setStatus(WorkStatus.Failed);
        jobDao.merge(tee);
        
        Long jobExecutionId = tee.getJobExecutionId();
        jobFailed(jobExecutionId);
    }

    @Override
    public void jobFailed(Long jobExecutionId) {
        JobExecutionEntity jee = jobDao.getEntity(JobExecutionEntity.class, jobExecutionId);
        jee.setDuration((System.currentTimeMillis() - jee.getStartTime()));
        jee.setStatus(WorkStatus.Failed);
        jobDao.merge(jee);
    }

    @Override
    public void jobSucceed(Long jobExecutionId) {
        JobExecutionEntity jee = jobDao.getEntity(JobExecutionEntity.class, jobExecutionId);
        jee.setDuration((System.currentTimeMillis() - jee.getStartTime()));
        jee.setStatus(WorkStatus.Succeed);
        
        jobDao.merge(jee);
    }

    @Override
    public JobExecutionDetailsList getJobExecutionList(Long jobId) {
        JobEntity je = jobDao.getEntity(JobEntity.class, jobId);
        Job job = ReflectionUtil.copy(Job.class, je);
        
        List<JobExecutionEntity> entities = jobDao.getJobExecutionList(jobId);
        JobExecutionDetailsList list = new JobExecutionDetailsList();
        list.setJob(job);
        for(JobExecutionEntity jee : entities) {
            JobExecutionDetails jed = ReflectionUtil.copy(JobExecutionDetails.class, jee);
            jed.setJob(job);
            list.getItems().add(jed);
        }
        return list;
    }

    @Override
    public JobExecutionDetails getJobExecutionDetails(Long jobExecutionId) {
        JobExecutionEntity entity = jobDao.getEntity(JobExecutionEntity.class, jobExecutionId);
        JobExecutionDetails details = ReflectionUtil.copy(JobExecutionDetails.class, entity);
        JobEntity jobEntity = jobDao.getEntity(JobEntity.class, entity.getJobId());
        Job job = ReflectionUtil.copy(Job.class, jobEntity);
        details.setJob(job);
        
        List<TaskExecutionEntity> tasks = jobDao.getJobExecutionChildren(jobExecutionId);
        for(TaskExecutionEntity tee : tasks) {
            TaskExecution te = ReflectionUtil.copy(TaskExecution.class, tee);
            details.getChildren().add(te);
        }
        
        List<RuntimeJobConfEntity> confList = jobDao.getJobExecutionConfList(jobExecutionId);
        for(RuntimeJobConfEntity conf : confList) {
            details.getRuntimeConfList().getItems().add(ReflectionUtil.copy(RuntimeJobConf.class, conf));
        }
        
        return details;
    }
}
