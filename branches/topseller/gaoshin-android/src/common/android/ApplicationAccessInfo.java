package common.android;

public class ApplicationAccessInfo {
    private long lastAccessTime;

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }
}
