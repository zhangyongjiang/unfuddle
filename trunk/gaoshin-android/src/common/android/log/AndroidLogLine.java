package common.android.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AndroidLogLine {
    private long time;
    private int pid;
    private String msg;
    private String tag;
    private LogLevel level;

    public AndroidLogLine() {
    }

    public AndroidLogLine(String line) {
        int pos = line.indexOf('/');

        String time = line.substring(0, pos - 2);
        setTime(time);

        String level = line.substring(pos - 1, pos);
        setLevel(LogLevel.get(level));

        int pos1 = line.indexOf('(', pos);

        String tag = line.substring(pos + 1, pos1);
        setTag(tag);

        int pos2 = line.indexOf(')', pos1);
        String pid = line.substring(pos1 + 1, pos2);
        setPid(Integer.parseInt(pid.trim()));

        String msg = line.substring(pos2 + 2);
        setMsg(msg);
    }

    public void setTime(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            int year = Calendar.getInstance().get(Calendar.YEAR);
            Date date = sdf.parse(year + "-" + s.trim());
            time = date.getTime();
        } catch (ParseException e) {
        }
    }
    
    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String ts = sdf.format(new Date(time));
        return ts + " " + getLevel() + "/" + tag + "(" + pid + "):" + msg;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public LogLevel getLevel() {
        return level;
    }

    public static void main(String[] args) throws ParseException {
        String line = "02-25 00:02:23.617 I/common.android.log.LogBlock( 5594): cannot parse log: --------- beginning of /dev/log/system. expect level is Error";
        AndroidLogLine log = new AndroidLogLine(line);
        System.out.println(new Date(log.getTime()));
    }
}

