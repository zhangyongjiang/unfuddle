package com.gaoshin.cloud.web.job.plugin.hadoop;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.job.bean.GaoshinProcess;
import com.gaoshin.cloud.web.job.bean.TaskProcessor;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;
import com.gaoshin.cloud.web.job.plugin.BaseTaskProcessor;
import common.util.reflection.ReflectionUtil;

@Service("hadoopTaskProcessor")
@Transactional
public class HadoopTaskProcessor extends BaseTaskProcessor implements TaskProcessor, ApplicationContextAware {
    private ApplicationContext springContext;

    @Override
    public void setApplicationContext(ApplicationContext springContext) throws BeansException {
        this.springContext = springContext;
    }
    
    @Override
    public String getName() {
        return "Hadoop Task";
    }

    @Override
    protected GaoshinProcess getProcess(JobExecutionEntity jee, TaskEntity te, TaskExecutionEntity tee) {
        HadoopProcess execTask = new HadoopProcess(springContext, 
                ReflectionUtil.copy(JobExecutionEntity.class, jee), 
                ReflectionUtil.copy(TaskEntity.class, te), 
                ReflectionUtil.copy(TaskExecutionEntity.class, tee));
        processList.add(execTask);
        return execTask;
    }
    
}
