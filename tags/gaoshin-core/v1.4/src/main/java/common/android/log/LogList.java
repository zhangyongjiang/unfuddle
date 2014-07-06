package common.android.log;

import java.util.ArrayList;
import java.util.List;

public class LogList {
    private List<LogBlock> list = new ArrayList<LogBlock>();

    public void setList(List<LogBlock> list) {
        this.list = list;
    }

    public List<LogBlock> getList() {
        return list;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("checking logs...\n");
        for(LogBlock lb : list) {
            sb.append(lb.toString());
        }
        return sb.toString();
    }
}
