package com.gaoshin.onsalelocal.osl.logging;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.MDC;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;

public class OslLogAppender extends JDBCAppender {
    private static Field msgField;
    static {
        try {
            msgField = LoggingEvent.class.getDeclaredField("message");
            msgField.setAccessible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public class LogThread extends Thread {
        public void run() {
            List<String> onerun = new ArrayList<String>();
            while(true) {
                try {
                    Thread.sleep(10000);
                }
                catch (Exception e) {
                }
                synchronized (buffer) {
                    if(buffer.size() == 0) {
                        try {
                            buffer.wait();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(buffer.size() == 0)
                        continue;
                    onerun.addAll(buffer);
                    buffer.clear();
                }
                try {
                    executeSqls(onerun);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    for(String sql : onerun) {
                    	System.out.println(sql);
                    }
                }
                onerun.clear();
            }
        }
    }
    
    private List<String> buffer = new ArrayList<String>();
    
    public OslLogAppender() {
        new LogThread().start();
    }
    
    protected void executeSqls(List<String> onerun) throws SQLException {
        Connection con = getConnection();
        if(con.isClosed()) {
        	connection = null;
        	con = getConnection();
        }
        Statement stmt = null;

        for(String sql : onerun) {
            try {
                stmt = con.createStatement();
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
               if (stmt != null)
                 stmt.close();
               throw e;
            }
            stmt.close();
        }
        
        closeConnection(con);
    }

    @Override
    public void append(LoggingEvent event) {
        event.getMDCCopy();
        if(MDC.get(MdcKeys.StartTime.name()) == null) {
            OslMdcFilter.resetMdc();
        }
        
        MDC.put(MdcKeys.LogTime.name(), String.valueOf(System.currentTimeMillis()));
        MDC.put(MdcKeys.LoggerName.name(), event.getLoggerName());
        Object msg = event.getMessage();
        if(msg != null && msgField != null) {
            String str = msg.toString();
            String[] stack = event.getThrowableStrRep();
            if(stack!=null) {
                str = str + " " + Arrays.toString(stack);
                event.getThrowableInformation().getThrowable().printStackTrace();
            }
            if(str.length() > 512) {
                str = str.substring(0, 512);
            }
            str = str.replaceAll("'", "''");
            if(msgField != null) {
                try {
                    msgField.set(event, str);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        super.append(event);
    }

    @Override
    protected void execute(String sql) throws SQLException {
        synchronized (buffer) {
            if(buffer.size() > 100000 || sql.contains(".js")) {
                // in case anything goes wrong, print sql
                // non api calls also goto stdout
                System.out.println(sql);
            }
            else {
                buffer.add(sql);
            }
            buffer.notifyAll();
        }
    }
    
}
