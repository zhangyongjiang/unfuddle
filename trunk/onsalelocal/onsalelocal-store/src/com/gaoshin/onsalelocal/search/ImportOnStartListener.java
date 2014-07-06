package com.gaoshin.onsalelocal.search;

import java.util.Properties;

import org.apache.solr.handler.dataimport.Context;

public class ImportOnStartListener extends ImportListener {

    @Override
    public void onEvent(Context ctx) {
        super.onEvent(ctx);
        
        String core = ctx.getSolrCore().getName();
        int solrid = getSolrId(ctx);
        
        try {
            Properties props = getProperties(ctx);
            Object startTime = null;
            startTime = getParam(KEY_START_TIME);
            if(startTime == null || "".equals(startTime))
                startTime = props.get(KEY_END_TIME);
            if(startTime == null) {
                startTime = System.currentTimeMillis();
            }
            if("true".equals(getParam("clean"))) {
                startTime = -1;
            }
            long endTime = System.currentTimeMillis();
            
            setParam(KEY_START_TIME, startTime);
            setParam(KEY_END_TIME, endTime);
            
            System.out.println(core + ": ======= startTime for this import is  " + startTime + " and endTime for this import is " + endTime);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int solrShards = 8;
        int solrid = 1;
        int solrIdSize = 1024 / solrShards;
        int idmin = solrid * solrIdSize;
        int idmax = idmin + solrIdSize;
        String str = String.format("%03X - %03X", idmin, idmax);
        System.out.println(str);
    }
}
