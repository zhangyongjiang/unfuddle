package com.gaoshin.coupon.android.model;

import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "Job",
        keyColumn = "id",
        autoId = true,
        create = {
                "create table job (" +
                        "id INTEGER primary key autoincrement " +
                        ", type text " +
                        ", param text " +
                        ", status text " +
                        ", start bigint " +
                        ", end bigint " +
                        ")"
        })
public class Job {
    private Integer id;
    private String type;
    private String param;
    private long start;
    private long end;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

}
