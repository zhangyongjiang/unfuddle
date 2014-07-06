package com.gaoshin.cloud.web.job.plugin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gaoshin.cloud.web.job.bean.GaoshinProcess;
import com.gaoshin.cloud.web.job.bean.TaskProcessor;
import com.gaoshin.cloud.web.job.bean.WorkStatus;
import com.gaoshin.cloud.web.job.dao.JobDao;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;

public abstract class BaseTaskProcessor implements TaskProcessor{
    abstract protected GaoshinProcess getProcess(JobExecutionEntity jee, TaskEntity te, TaskExecutionEntity tee);
    
    @Autowired private JobDao jobDao;
    
    protected List<GaoshinProcess> processList = new ArrayList<GaoshinProcess>();
    
    @Override
    public void run(JobExecutionEntity jee, TaskEntity toBeExecuted) {
        TaskExecutionEntity tee = new TaskExecutionEntity();
        tee.setJobExecutionId(jee.getId());
        tee.setArgs(toBeExecuted.getArgs());
        tee.setErrorHandlingTask(toBeExecuted.isErrorHandlingTask());
        tee.setExecOrder(toBeExecuted.getExecOrder());
        tee.setExpectedDuration(toBeExecuted.getExpectedDuration());
        tee.setHandler(toBeExecuted.getHandler());
        tee.setTaskId(toBeExecuted.getId());
        tee.setName(toBeExecuted.getName());
        tee.setNumOfRetries(toBeExecuted.getNumOfRetries());
        tee.setRetryInterval(toBeExecuted.getRetryInterval());
        tee.setStartTime(System.currentTimeMillis());
        tee.setStatus(WorkStatus.Running);
        jobDao.saveEntity(tee);
        
        GaoshinProcess process = getProcess(jee, toBeExecuted, tee);
        process.start();
    }

}
