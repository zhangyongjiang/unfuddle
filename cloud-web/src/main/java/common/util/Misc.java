package common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Misc {
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String getCurrentThreadStackTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
