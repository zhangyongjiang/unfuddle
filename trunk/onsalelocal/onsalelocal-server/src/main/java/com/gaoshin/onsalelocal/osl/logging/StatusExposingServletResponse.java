package com.gaoshin.onsalelocal.osl.logging;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.MDC;

public class StatusExposingServletResponse extends HttpServletResponseWrapper {

    private int httpStatus = -1;

    public StatusExposingServletResponse(HttpServletResponse response) {
        super(response);
    }
    
    @Override
    public void sendError(int sc) throws IOException {
        MDC.put(MdcKeys.Status.name(), sc);
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        MDC.put(MdcKeys.Status.name(), sc);
        httpStatus = sc;
        super.sendError(sc, msg);
    }

    @Override
    public void setStatus(int sc) {
        MDC.put(MdcKeys.Status.name(), sc);
        httpStatus = sc;
        super.setStatus(sc);
    }
    
    @Override
    public void sendRedirect(String location) throws IOException {
        httpStatus = 302;
        MDC.put(MdcKeys.Status.name(), httpStatus);
        super.sendRedirect(location);
    }
    
    public int getStatus() {
        return httpStatus;
    }
    
    @Override
    public void setStatus(int sc, String sm) {
        httpStatus = sc;
        MDC.put(MdcKeys.Status.name(), httpStatus);
        super.setStatus(sc, sm);
    }
}
