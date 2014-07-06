package com.gaoshin.cloud.web.job.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationContext;

import com.gaoshin.cloud.web.job.bean.GaoshinProcess;
import com.gaoshin.cloud.web.job.bean.WorkStatus;
import com.gaoshin.cloud.web.job.dao.JobDao;
import com.gaoshin.cloud.web.job.entity.JobExecutionEntity;
import com.gaoshin.cloud.web.job.entity.Parameter;
import com.gaoshin.cloud.web.job.entity.RuntimeJobConfEntity;
import com.gaoshin.cloud.web.job.entity.TaskEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionEntity;
import com.gaoshin.cloud.web.job.entity.TaskExecutionTryEntity;
import com.gaoshin.cloud.web.job.schedule.JobExecutionManager;

public abstract class BaseProcess implements GaoshinProcess{
    private static final String SetConfPrefix = "__set_conf__:";
    
    abstract protected void doJob(String[] args) throws Exception;
    
    protected ApplicationContext springContext;
    
    protected JobExecutionEntity jobExecutionEntity;
    protected TaskEntity taskEntity;
    protected TaskExecutionEntity taskExecutionEntity;
    
    protected JobDao jobDao;
    protected TaskExecutionTryEntity currentTry;

    public BaseProcess(ApplicationContext springContext, JobExecutionEntity jee, TaskEntity te, TaskExecutionEntity tee) {
        this.springContext = springContext;
        this.jobExecutionEntity = jee;
        this.taskEntity = te;
        this.taskExecutionEntity = tee;
        
        jobDao = springContext.getBean(JobDao.class);
    }

    protected void error(Exception e) {
        currentTry.appendNote(e);
        currentTry.setStatus(WorkStatus.Failed);
        jobDao.merge(currentTry);
        retry();
    }

    public void error(int exitCode) {
        currentTry.appendNote("task failed with exit code " + exitCode);
        currentTry.setStatus(WorkStatus.Failed);
        jobDao.merge(currentTry);
        cleanup();
        retry();
    }

    private void retry() {
        if(taskExecutionEntity.getActualNumOfRetries() >= taskEntity.getNumOfRetries()) {
            JobExecutionManager manager = springContext.getBean(JobExecutionManager.class);
            manager.taskExecutionFailed(taskExecutionEntity.getId());
            
            taskExecutionEntity.appendLogMsg(currentTry.getStdout());
            taskExecutionEntity.appendLogMsg(currentTry.getNote());
            jobDao.merge(taskExecutionEntity);
            
            return;
        }
        if(taskExecutionEntity.getRetryInterval() > 0) {
            try {
                Thread.sleep(taskExecutionEntity.getRetryInterval());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        start();
    }

    public void succeed() {
        cleanup();
        currentTry.setStatus(WorkStatus.Succeed);
        jobDao.merge(currentTry);
        
        taskExecutionEntity.setNextTaskExecOrder(currentTry.getNextTaskExecOrder());
        taskExecutionEntity.appendLogMsg(currentTry.getStdout());
        taskExecutionEntity.appendLogMsg(currentTry.getNote());
        taskExecutionEntity.setNextTaskExecOrder(currentTry.getNextTaskExecOrder());
        jobDao.merge(taskExecutionEntity);

        if(jobExecutionEntity.getNote() != null) {
            List<RuntimeJobConfEntity> confList = jobDao.getJobExecutionConfList(jobExecutionEntity.getId());
            Map<String, String> map = new HashMap<String, String>();
            for(RuntimeJobConfEntity jece : confList) {
                map.put(jece.getCkey(), jece.getCvalue());
            }
            String tmpKey = UUID.randomUUID().toString();
            map.put(tmpKey, jobExecutionEntity.getNote());
            map = Parameter.replace(map);
            jobExecutionEntity.setNote(map.get(tmpKey));
            jobDao.merge(jobExecutionEntity);
        }

        JobExecutionManager manager = springContext.getBean(JobExecutionManager.class);
        manager.taskExecutionSucceed(taskExecutionEntity.getId());
    }
    
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exec();
                }
                catch (Exception e) {
                    error(e);
                }
            }
        }).start();
    }
    
    protected void exec() throws Exception {
        taskExecutionEntity.setActualNumOfRetries(taskExecutionEntity.getActualNumOfRetries() + 1);
        jobDao.merge(taskExecutionEntity);
        
        currentTry = new TaskExecutionTryEntity();
        currentTry.setStartTime(System.currentTimeMillis());
        currentTry.setStatus(WorkStatus.Running);
        currentTry.setTaskExecutionId(taskExecutionEntity.getId());
        jobDao.saveEntity(currentTry);
        
        String cmd = taskExecutionEntity.getArgs();
        String[] params = parse(cmd);
        
        String output = null;
        for (int i = 0; i < params.length; i++) {
            if("-outputstream".equals(params[i])) {
                output = params[i+1];
                break;
            }
        }
        
        if(output != null) {
            String[] actual = new String[params.length - 2];
            for(int i = 0, j=0; i < params.length; i++) {
                if("-outputstream".equals(params[i])) {
                    i++;
                }
                else {
                    actual[j++] = params[i];
                }
            }
            params = actual;
        }
        
        replaceParams(params);
        doJob(params);
        
        if(output != null) {
            OutputStream os = getOutputStream();
            os.write(output.getBytes());
            os.close();
        }
        
        catchStdout();
        catchStderr();
        jobDao.merge(currentTry);
        
        int exitCode = getExitCode();
        if(exitCode != 0) {
            error(exitCode);
        }
        else {
            succeed();
        }
    }
    
    private void replaceParams(String... params) {
        List<RuntimeJobConfEntity> confList = jobDao.getJobExecutionConfList(jobExecutionEntity.getId());
        Map<String, String> map = new HashMap<String, String>();
        for(RuntimeJobConfEntity jece : confList) {
            map.put(jece.getCkey(), jece.getCvalue());
        }
        
        String tmpKey = UUID.randomUUID().toString();
        for(int i=0; i<params.length; i++) {
            map.put(tmpKey+i, params[i]);
        }
        map = Parameter.replace(map);
        for(int i=0; i<params.length; i++) {
            params[i] = map.get(tmpKey+i);
        }
    }
    
    protected void catchStderr() throws IOException {
        InputStream errStream = getErrorStream();
        StringBuilder sb = new StringBuilder();
        byte[] buff = new byte[8192];
        while(true) {
            int len = errStream.read(buff);
            if(len < 0) {
                break;
            }
            String stderr = new String(buff, 0, len);
            sb.append(stderr);
        }
        currentTry.appendNote(sb.toString());
    }

    protected void catchStdout() throws IOException {
        InputStream inputStream = getInputStream();
        byte[] buff = new byte[8192];
        StringBuilder sb = new StringBuilder();
        while(true) {
            int len = inputStream.read(buff);
            if(len < 0) {
                break;
            }
            String stdout = new String(buff, 0, len);
            sb.append(stdout);
        }
        if(sb.length() > 0) {
            String stdout = sb.toString();
            currentTry.appendStdout(stdout);
            parseOutput(stdout);
        }
    }

    private void parseOutput(String stdout) {
        if(stdout != null) {
            BufferedReader br = new BufferedReader(new StringReader(stdout));
            while(true) {
                String line;
                try {
                    line = br.readLine();
                    if(line == null) {
                        break;
                    }
                    if(line.startsWith(SetConfPrefix)) {
                        String keyValue = line.substring(SetConfPrefix.length());
                        int pos = keyValue.indexOf("=");
                        String key = keyValue.substring(0, pos);
                        String value = "";
                        pos++;
                        if(pos < keyValue.length() - 1) {
                            value = keyValue.substring(pos);
                        }
                        if("nextTaskExecOrder".equals(key)) {
                            try {
                                int nextTaskExecOrder = Integer.parseInt(value);
                                currentTry.setNextTaskExecOrder(nextTaskExecOrder);
                                jobDao.saveEntity(currentTry);
                            }
                            catch (Exception e) {
                            }
                        }
                        setJobExecutionConf(key, value);
                    }
                }
                catch (IOException e) {
                    break;
                }
            }
        }
    }

    private void setJobExecutionConf(String key, String value) {
        RuntimeJobConfEntity jece = jobDao.getJobExecutionConf(jobExecutionEntity.getId(), key);
        if(jece == null) {
            jece = new RuntimeJobConfEntity();
            jece.setCkey(key);
            jece.setCvalue(value);
            jece.setJobExecutionId(jobExecutionEntity.getId());
            jobDao.saveEntity(jece);
        }
        else {
            jece.setCvalue(value);
            jobDao.saveEntity(jece);
        }
    }

    private String[] parse(String oneline) {
        ArrayList<String> argList = new ArrayList<String>();

        boolean singleQuote = false;
        boolean doubleQuote = false;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<oneline.length(); i++) {
            char c = oneline.charAt(i);
            if(c == '\'') {
                if(doubleQuote) {
                    sb.append(c);
                }
                else {
                    if(singleQuote) {
                        singleQuote = false;
                        if(sb.length() > 0) {
                            argList.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                    else {
                        singleQuote = true;
                        if(sb.length() > 0) {
                            argList.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                }
            }
            else if(c == '"') {
                if(singleQuote) {
                    sb.append(c);
                }
                else {
                    if(doubleQuote) {
                        doubleQuote = false;
                        if(sb.length() > 0) {
                            argList.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                    else {
                        doubleQuote = true;
                        if(sb.length() > 0) {
                            argList.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                }
            }
            else if (c == ' ') {
                if(singleQuote || doubleQuote) {
                    sb.append(c);
                }
                else if(sb.length() > 0) {
                    argList.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
            else {
                sb.append(c);
            }
        }
        if(sb.length() > 0) {
            argList.add(sb.toString());
        }

        String[] ret = new String[argList.size()];
        for (int i = 0; i < argList.size(); i++) {
            ret[i] = argList.get(i);
        }
        return ret;
    }
}
