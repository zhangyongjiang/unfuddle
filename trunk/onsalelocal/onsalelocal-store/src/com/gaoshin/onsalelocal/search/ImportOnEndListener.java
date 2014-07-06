package com.gaoshin.onsalelocal.search;

import org.apache.solr.handler.dataimport.Context;

public class ImportOnEndListener extends ImportListener {

    @Override
    public void onEvent(Context ctx) {
        String name = ctx.getSolrCore().getName();
        Object value = ctx.getRequestParameters().get(KEY_END_TIME);
        saveProperties(ctx, KEY_END_TIME, value.toString());
        System.out.println(name + ": ======= import finished. Time spent: " + (System.currentTimeMillis() - Long.parseLong(value.toString())) + ". startTime for next import is " + value +  ". import stats: " + ctx.getStats());
    }

}
