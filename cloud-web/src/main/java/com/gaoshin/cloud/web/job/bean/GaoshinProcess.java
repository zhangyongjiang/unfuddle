package com.gaoshin.cloud.web.job.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface GaoshinProcess {
    int getExitCode() throws Exception;
    InputStream getInputStream();
    InputStream getErrorStream() throws IOException;
    OutputStream getOutputStream();
    void start();
    String getProcessInfo();
    void cleanup();
}
