package com.gaoshin.top;

import com.gaoshin.sorma.annotation.Ddl;
import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "job_execution",
        keyColumn = "id",
        autoId = true,
        create = {
            "create table if not exists job_execution (" +
                    " id integer PRIMARY KEY autoincrement" +
                    ", job integer" +
                    ", startTime long" +
                    ", status varchar(32)" +
                    ")"
        },
        ddls = {
            @Ddl(
                    yyyyMMddHHmmss = "20110525000000",
                    value = "create table if not exists job_execution (" +
                            " id integer PRIMARY KEY autoincrement" +
                            ", job integer" +
                            ", startTime long" +
                            ", status varchar(32)" +
                            ")"
            )
        })
public class JobExecution {
    private Integer id;
    private Integer job;
    private long startTime;
    private JobStatus status;

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

}
