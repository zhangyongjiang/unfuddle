package common.android;

public enum MsgType {
    Web,
    Conf,
    Notification,
    Location,
    Insert,
    Update,
    Delete,
    Message,
    Log;

    public static MsgType getType(String type) {
        for (MsgType mt : MsgType.class.getEnumConstants()) {
            if (mt.name().equals(type))
                return mt;
        }
        return null;
    }
}

