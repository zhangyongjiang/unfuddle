package com.gaoshin.cloud.web.job.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.cloud.web.bean.GenericResponse;
import com.gaoshin.cloud.web.bean.StringList;
import com.gaoshin.cloud.web.job.bean.Job;
import com.gaoshin.cloud.web.job.bean.JobConf;
import com.gaoshin.cloud.web.job.bean.JobConfDetails;
import com.gaoshin.cloud.web.job.bean.JobConfList;
import com.gaoshin.cloud.web.job.bean.JobDependency;
import com.gaoshin.cloud.web.job.bean.JobDependencyDetails;
import com.gaoshin.cloud.web.job.bean.JobDetails;
import com.gaoshin.cloud.web.job.bean.JobExecution;
import com.gaoshin.cloud.web.job.bean.JobExecutionDetails;
import com.gaoshin.cloud.web.job.bean.JobExecutionDetailsList;
import com.gaoshin.cloud.web.job.bean.JobList;
import com.gaoshin.cloud.web.job.bean.Task;
import com.gaoshin.cloud.web.job.bean.TaskDetails;
import com.gaoshin.cloud.web.job.schedule.JobExecutionManager;
import com.gaoshin.cloud.web.job.schedule.JobScheduler;
import com.gaoshin.cloud.web.job.service.JobService;
import common.util.web.JerseyBaseResource;

@Path("/job")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class JobResource extends JerseyBaseResource {
    @Autowired private JobService jobService;
    @Autowired private JobExecutionManager jobExecutionManager;
    @Autowired private JobScheduler jobScheduler;

    @POST
    @Path("create")
    public Job ls(Job job) {
        return jobService.create(job);
    }
    
    @POST
    @Path("update")
    public Job update(Job job) {
        return jobService.update(job);
    }
    
    @POST
    @Path("enable/{jobId}/{enable}")
    public GenericResponse enable(@PathParam("jobId")Long jobId, @PathParam("enable")boolean enable) {
        jobService.enableJob(jobId, enable);
        return new GenericResponse();
    }
    
    @POST
    @Path("delete/{jobId}")
    public GenericResponse delete(@PathParam("jobId") Long jobId) {
        jobService.delete(jobId);
        return new GenericResponse();
    }
    
    @POST
    @Path("run/{jobId}")
    public JobExecution run(@PathParam("jobId") Long jobId, JobConfList confList) {
        return jobScheduler.runJob(jobId, confList);
    }
    
    @GET
    @Path("list") 
    public JobList list() {
        return jobService.list();
    }
    
    @GET
    @Path("details") 
    public JobDetails getJobDetails(@QueryParam("id") Long jobId) {
        return jobService.getJobDetails(jobId);
    }

    @POST
    @Path("job-conf/create")
    public JobConf create(JobConf jobConf) {
        return jobService.createJobConf(jobConf);
    }
    
    @GET
    @Path("job-conf/details") 
    public JobConfDetails getJobConfDetails(@QueryParam("id") Long jobConfId) {
        return jobService.getJobConfDetails(jobConfId);
    }
    
    @POST
    @Path("job-conf/update") 
    public GenericResponse updateJobConf(JobConf jobConf) {
        jobService.updateJobConfDetails(jobConf);
        return new GenericResponse();
    }
    
    @POST
    @Path("job-conf/delete/{jobConfId}") 
    public GenericResponse deleteJobConf(@PathParam("jobConfId")Long jobConfId) {
        jobService.deleteJobConf(jobConfId);
        return new GenericResponse();
    }

    @GET
    @Path("task/type-list") 
    public StringList listTaskType(@QueryParam("id") Long taskId) {
        return jobExecutionManager.listTaskType();
    }

    @POST
    @Path("task/create")
    public Task create(Task task) {
        return jobService.createTask(task);
    }
    
    @GET
    @Path("task/details") 
    public TaskDetails getTaskDetails(@QueryParam("id") Long taskId) {
        return jobService.getTaskDetails(taskId);
    }
    
    @POST
    @Path("task/update") 
    public GenericResponse updateTask(Task task) {
        jobService.updateTaskDetails(task);
        return new GenericResponse();
    }
    
    @POST
    @Path("task/delete/{taskId}") 
    public GenericResponse deleteTask(@PathParam("taskId")Long taskId) {
        jobService.deleteTask(taskId);
        return new GenericResponse();
    }

    @POST
    @Path("job-dependency/create")
    public JobDependency create(JobDependency jobDependency) {
        return jobService.createJobDependency(jobDependency);
    }
    
    @GET
    @Path("job-dependency/details") 
    public JobDependencyDetails getJobDependencyDetails(@QueryParam("id") Long jobDependencyId) {
        return jobService.getJobDependencyDetails(jobDependencyId);
    }
    
    @POST
    @Path("job-dependency/update") 
    public GenericResponse updateJobDependency(JobDependency jobDependency) {
        jobService.updateJobDependencyDetails(jobDependency);
        return new GenericResponse();
    }
    
    @POST
    @Path("job-dependency/delete/{jobDependencyId}") 
    public GenericResponse deleteJobDependency(@PathParam("jobDependencyId")Long jobDependencyId) {
        jobService.deleteJobDependency(jobDependencyId);
        return new GenericResponse();
    }
    
    @GET
    @Path("job-execution/list")
    public JobExecutionDetailsList getJobExecutionList(@QueryParam("jobId")Long jobId) {
        return jobExecutionManager.getJobExecutionList(jobId);
    }
    
    @GET
    @Path("job-execution/details")
    public JobExecutionDetails getJobExecutionDetails(@QueryParam("jobExecutionId")Long jobExecutionId) {
        return jobExecutionManager.getJobExecutionDetails(jobExecutionId);
    }
}
