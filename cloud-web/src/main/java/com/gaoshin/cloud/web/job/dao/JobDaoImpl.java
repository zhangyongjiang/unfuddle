package com.gaoshin.cloud.web.job.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.gaoshin.cloud.web.dao.HibernateBaseDao;
import com.gaoshin.cloud.web.job.bean.WorkStatus;
import com.gaoshin.cloud.web.job.entity.JobConfEntity;
import com.gaoshin.cloud.web.job.entity.JobDependencyEntity;
import com.gaoshin.cloud.web.job.entity.JobEntity;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.RuntimeJobConfEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;

@Repository("jobDao")
public class JobDaoImpl extends HibernateBaseDao implements JobDao {

    @Override
    public List<JobDependencyEntity> getDownstreamJobs(Long jobId) {
        return find("from JobDependencyEntity jde where jde.upstreamJobId=?", jobId);
    }

    @Override
    public List<JobExecutionEntity> getDueJobExecutions() {
        return find("from JobExecutionEntity jee where jee.scheduledStartTime<? and jee.status=?", 
                System.currentTimeMillis(), 
                WorkStatus.Pending);
    }

    @Override
    public List<JobEntity> getUpstreams(final Long jobId) {
        return hibernateTemplate.execute(new HibernateCallback<List<JobEntity>>() {
            @Override
            public List<JobEntity> doInHibernate(Session session) throws HibernateException, SQLException {
                String sql = "select * from JobEntity je, JobDependencyEntity jde where je.id=jde.upstreamJobId and jde.jobId=?";
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.addEntity(JobDependencyEntity.class);
                sqlQuery.setParameter(0, jobId);
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<JobDependencyEntity> getDependencies(Long jobId) {
        return find(JobDependencyEntity.class, "from JobDependencyEntity jde where jde.jobId=?", jobId);
    }

    @Override
    public List<JobExecutionEntity> getJobExecution(Long upstreamJobId, long upstreamStartTime, WorkStatus status) {
        if(status == null) {
            return find("from JobExecution je where je.jobId=? and je.scheduledStartTime=?", upstreamJobId, upstreamStartTime);
        }
        else {
            return find("from JobExecution je where je.jobId=? and je.scheduledStartTime=? and je.status=?", upstreamJobId, upstreamStartTime, status);
        }
    }

    @Override
    public List<TaskEntity> getOrderedJobTasks(Long jobId) {
        return find("from TaskEntity te where te.jobId = ? order by te,execOrder", jobId);
    }

    @Override
    public List<JobConfEntity> getJobConfList(Long jobId) {
        return find("from JobConfEntity jce where jce.jobId=?", jobId);
    }

    @Override
    public List<JobExecutionEntity> getJobExecutionList(Long jobId) {
        return find("from JobExecutionEntity jee where jee.jobId=? order by jee.scheduledStartTime desc", jobId);
    }

    @Override
    public List<TaskExecutionEntity> getJobExecutionChildren(Long jobExecutionId) {
        return find("from TaskExecutionEntity tee where tee.jobExecutionId=? order by tee.startTime", jobExecutionId);
    }

    @Override
    public List<JobEntity> getEnabledCronJobs() {
       return find(JobEntity.class, "from JobEntity je where je.enabled = ? and je.cronExpression is not null", true);
    }

    @Override
    public JobExecutionEntity getLastCronScheduledJobExecution(Long jobId) {
        List<JobExecutionEntity> list = find(JobExecutionEntity.class, "from JobExecutionEntity jee where jee.scheduledStartTime > 0 and jee.jobId = ? order by jee.scheduledStartTime desc limit 1", jobId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public RuntimeJobConfEntity getJobExecutionConf(Long jobExecutionId, String key) {
        List<RuntimeJobConfEntity> list = find("from RuntimeJobConfEntity jece where jece.jobExecutionId=? and jece.ckey=?", jobExecutionId, key);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<RuntimeJobConfEntity> getJobExecutionConfList(Long jobExecutionId) {
        List<RuntimeJobConfEntity> list = find("from RuntimeJobConfEntity jece where jece.jobExecutionId=? ", jobExecutionId);
        return list;
    }

}