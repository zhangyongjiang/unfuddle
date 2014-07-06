package common.android;

import java.util.ArrayList;
import java.util.List;

public class AllowedDomainList {
    private List<String> domains = new ArrayList<String>();

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public List<String> getDomains() {
        return domains;
    }
}
