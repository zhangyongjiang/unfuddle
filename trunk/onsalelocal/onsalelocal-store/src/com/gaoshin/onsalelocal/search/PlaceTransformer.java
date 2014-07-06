package com.gaoshin.onsalelocal.search;

import java.util.Map;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.Transformer;

public class PlaceTransformer extends Transformer {
    private static DataImportTransformer[] transformers = {
            new AddressTransformer(),
            new NameTransformer(),
            new PhoneTransformer(),
            new IdTransformer(),
    };

    public boolean transform(Map row) {
        boolean keep = true;
        for(DataImportTransformer transformer : transformers) {
            keep = transformer.transform(row);
            if(!keep) break;
        }
        return keep;
    }

    @Override
    public Object transformRow(Map<String, Object> arg0, Context arg1) {
        boolean keep = transform(arg0);
        return keep ? arg0 : null;
    }

}
