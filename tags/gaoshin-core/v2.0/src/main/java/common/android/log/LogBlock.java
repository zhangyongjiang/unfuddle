package common.android.log;

import java.util.ArrayList;
import java.util.List;

public class LogBlock {
    private List<AndroidLogLine> list = new ArrayList<AndroidLogLine>();

    public LogBlock addLine(long after, String tag, String pattern, String line) {
        AndroidLogLine log = null;
        try {
            log = new AndroidLogLine(line);
        } catch (Exception e) {
            return null;
        }
        
        if(log.getTime()<after) {
            return null;
        }
        
        if(!log.getTag().equals(tag) && !line.contains(pattern)) {
            if(list.size() == 0) {
                return null;
            }
            AndroidLogLine last = list.get(list.size() - 1);
            if(last.getPid() == log.getPid()) {
                getList().add(log);
            }
            return null;
        }
        
        if (getList().size() == 0) {
            getList().add(log);
            return null;
        }

        AndroidLogLine last = getList().get(getList().size() - 1);
        if (last.getPid() == log.getPid()) {
            getList().add(log);
            return null;
        }

        LogBlock block = new LogBlock();
        block.getList().add(log);
        return block;
    }

    public void setList(List<AndroidLogLine> lines) {
        this.list = lines;
    }

    public List<AndroidLogLine> getList() {
        return list;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(AndroidLogLine log : getList()) {
            sb.append(log.toString()).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
