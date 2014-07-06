package com.gaoshin.groupon.api;

import java.net.URLEncoder;

import com.gaoshin.Api;

public class GrouponApi extends Api {
    private static String clientId = "3ea270b8be4d62fc96824dee239ee84958a0bdf3";
/*
 * http://api.groupon.com/v2/channels/goods/deals?client_id=3ea270b8be4d62fc96824dee239ee84958a0bdf3
 * http://api.groupon.com/v2/divisions?client_id=3ea270b8be4d62fc96824dee239ee84958a0bdf3
 * http://api.groupon.com/v2/deals?client_id=3ea270b8be4d62fc96824dee239ee84958a0bdf3&division_id=abilene
 * http://api.groupon.com/v2/deals/g1gd-total-gym-xls-abilene-tx?client_id=3ea270b8be4d62fc96824dee239ee84958a0bdf3
 */
    
    public static DivisionsResponse listDivisions() throws Exception {
        String url = "http://api.groupon.com/v2/divisions.xml?client_id=" + clientId;
        return execute(url, DivisionsResponse.class);
    }
    
    public static DealsResponse findDeals(float lat, float lng, int radius) throws Exception {
        String url = "http://api.groupon.com/v2/deals.xml?client_id=" + clientId + "&lat=" + lat + "&lng=" + lng + "&radius=" + radius;
        return execute(url, DealsResponse.class);
    }
    
    public static DealsResponse findDeals(String divisionId) throws Exception {
        String url = "http://api.groupon.com/v2/deals.xml?client_id=" + clientId + "&division_id=" + URLEncoder.encode(divisionId, "UTF-8");
        return execute(url, DealsResponse.class);
    }

    public static void main(String[] args) throws Exception {
//        DivisionsResponse resp = listDivisions();
        DealsResponse deals = findDeals(37.4005f, -122.073f, 40);
        dumpXml(deals);
    }

    public static DealResponse getDealDetails(String id) throws Exception {
        String url = "http://api.groupon.com/v2/deals/" + id + ".xml?client_id=" + clientId;
        return execute(url, DealResponse.class);
    }
}
