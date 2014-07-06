package com.gaoshin.cloud.web.job.plugin.hadoop;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.context.ApplicationContext;

import com.gaoshin.cloud.web.job.bean.GaoshinProcess;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;
import com.gaoshin.cloud.web.job.plugin.BaseProcess;
import common.util.ssh.SshShell;

public class HadoopProcess extends BaseProcess implements GaoshinProcess {
    private SshShell sshShell;

    public HadoopProcess(ApplicationContext springContext, JobExecutionEntity jee, TaskEntity te, TaskExecutionEntity tee) {
        super(springContext, jee, te, tee);
    }
    
    @Override
    public void start() {
        try {
            sshShell = new SshShell("74.208.209.155", "hadoop", "2wsx@WSX");
            sshShell.open();
        }
        catch (Exception e) {
        }
        super.start();
    }

    public void doJob(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        for(String s : args) {
            sb.append(s).append(" ");
        }
        String cmd = sb.toString();
        sshShell.run(cmd);
    }

    @Override
    public int getExitCode() throws Exception {
        return sshShell.getExitCode();
    }

    @Override
    public InputStream getInputStream() {
        return sshShell.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return sshShell.getOutputStream();
    }

    @Override
    public String getProcessInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getErrorStream() throws IOException {
        return sshShell.getStderrInputStream();
    }

    @Override
    public void cleanup() {
        try {
            sshShell.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
