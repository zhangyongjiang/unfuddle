package com.gaoshin.stock.model;

import com.gaoshin.sorma.annotation.ContentProvider;
import com.gaoshin.sorma.annotation.SormaContentProvider;
import com.gaoshin.stock.plugin.Plugin;

@ContentProvider(
        version = 1,
        mappingClasses = {
                Configuration.class,
                Quote.class,
                StockGroup.class,
                GroupItem.class,
                Plugin.class
        }
)
public class StockContentProvider extends SormaContentProvider {

}
