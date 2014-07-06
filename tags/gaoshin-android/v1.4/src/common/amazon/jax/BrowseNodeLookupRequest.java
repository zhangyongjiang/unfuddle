
package common.amazon.jax;

import java.util.ArrayList;
import java.util.List;


public class BrowseNodeLookupRequest {
    protected List<String> browseNodeId;
    protected List<String> responseGroup;

    public List<String> getBrowseNodeId() {
        if (browseNodeId == null) {
            browseNodeId = new ArrayList<String>();
        }
        return this.browseNodeId;
    }

    public List<String> getResponseGroup() {
        if (responseGroup == null) {
            responseGroup = new ArrayList<String>();
        }
        return this.responseGroup;
    }

}
