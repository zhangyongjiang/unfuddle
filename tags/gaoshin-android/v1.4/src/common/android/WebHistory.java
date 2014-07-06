package common.android;

import java.util.ArrayList;
import java.util.List;

public class WebHistory {
    private List<String> list = new ArrayList<String>();

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void add(String url) {
        if (list.contains(url))
            return;
        list.add(url);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = list.size() - 1; i >= 0; i--) {
            sb.append(list.get(i));
            if (i != 0)
                sb.append("\n");
        }
        return sb.toString();
    }

    public void fromString(String str) {
        for (String s : str.split("[\n\r]+")) {
            if (s.length() == 0)
                continue;
            list.add(0, s);
        }
    }
}
