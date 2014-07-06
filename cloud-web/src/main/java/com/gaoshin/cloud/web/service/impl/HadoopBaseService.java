package com.gaoshin.cloud.web.service.impl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;

public class HadoopBaseService {
    public String getHdfsUrl() {
        return "hdfs://74.208.209.155:9000";
    }
    
    public Configuration getConf() {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", getHdfsUrl());
        conf.set("hadoop.job.ugi", "hadoop");
        return conf;
    }

    public DFSClient getDfsClient() throws IOException {
        Configuration conf = getConf();
        DFSClient dfsClient = new DFSClient(conf);
        return dfsClient;
    }
}
