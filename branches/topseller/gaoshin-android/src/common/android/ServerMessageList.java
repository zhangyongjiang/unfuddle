package common.android;

import java.util.ArrayList;
import java.util.List;

public class ServerMessageList {
    private List<GenericMessage> list = new ArrayList<GenericMessage>();

    public void setList(List<GenericMessage> list) {
        this.list = list;
    }

    public List<GenericMessage> getList() {
        return list;
    }
}
