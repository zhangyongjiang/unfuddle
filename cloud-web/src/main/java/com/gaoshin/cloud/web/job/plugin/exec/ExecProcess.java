package com.gaoshin.cloud.web.job.plugin.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.context.ApplicationContext;

import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;
import com.gaoshin.cloud.web.job.plugin.BaseProcess;

public class ExecProcess extends BaseProcess {
    private Process process = null;

    public ExecProcess(ApplicationContext springContext, JobExecutionEntity jee, TaskEntity te, TaskExecutionEntity tee) {
        super(springContext, jee, te, tee);
    }

    @Override
    public void doJob(String[] args) throws Exception {
        this.process = Runtime.getRuntime().exec(args);
    }

    @Override
    public int getExitCode() throws Exception {
        return process.exitValue();
    }

    @Override
    public InputStream getInputStream() {
        return process.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }

    @Override
    public String getProcessInfo() {
        return null;
    }

    @Override
    public InputStream getErrorStream() throws IOException {
        return process.getErrorStream();
    }

    @Override
    public void cleanup() {
    }
}
