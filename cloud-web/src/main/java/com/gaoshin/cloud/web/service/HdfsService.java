package com.gaoshin.cloud.web.service;

import java.io.IOException;
import java.io.InputStream;

import com.gaoshin.cloud.web.bean.hadoop.HdfsFile;

public interface HdfsService {
    HdfsFile ls(String path) throws IOException;

    HdfsFile info(String path, Long size) throws IOException;

    void upload(String path, InputStream inputStream) throws IOException;

    void download(String path) throws IOException;

    void mkdir(String path) throws IOException;

    void rm(String string) throws IOException;
}
