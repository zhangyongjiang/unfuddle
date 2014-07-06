package common.android.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogReader {
    public static LogList read(LogLevel level, long after, String tag, String pattern) throws IOException {
        String[] cmd = {"logcat", "-d", "-v", "time"};
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream inputStream = process.getInputStream();
        List<LogBlock> logs = read(level, after, tag, pattern, inputStream);
        inputStream.close();
        
        LogList list = new LogList();
        list.setList(logs);
        return list;
    }
    
    private static List<LogBlock> read(LogLevel level, long after, String tag, String pattern, InputStream input)
            throws IOException {
        List<LogBlock> logs = new ArrayList<LogBlock>();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        LogBlock current = new LogBlock();
        logs.add(current);
        while(true) {
            String line = br.readLine();
            if(line == null)
                break;
            if (line.indexOf(pattern) != -1) {
            }
            int oldSize = current.getList().size();
            LogBlock newBlock = current.addLine(level, after, tag, pattern, line);
            if(newBlock!=null) {
                logs.add(newBlock);
                current = newBlock;
            } else {
                int newSize = current.getList().size();
                if (newSize > oldSize) {
                }
            }
        }
        if(logs.size() == 1 && logs.get(0).getList().size() == 0) {
            return new ArrayList<LogBlock>();
        }
        else {
            return logs;
        }
    }

}
