package common.android.log;

public enum LogLevel {
    Verbose,
    Debug,
    Info,
    Warning,
    Error,
    Silent;

    public static LogLevel get(String s) {
        if (s == null)
            return null;
        s = s.toLowerCase();
        for (LogLevel level : LogLevel.class.getEnumConstants()) {
            if (level.name().toLowerCase().startsWith(s)) {
                return level;
            }
        }
        return null;
    }
}
