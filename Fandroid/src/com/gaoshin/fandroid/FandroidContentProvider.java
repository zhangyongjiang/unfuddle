package com.gaoshin.fandroid;

import com.gaoshin.sorma.AnnotatedORM;
import com.gaoshin.sorma.annotation.AbstractORMContentProvider;
import com.gaoshin.sorma.annotation.AnnotatedORMDefinition;
import com.gaoshin.sorma.annotation.ORMMapping;
import com.gaoshin.sorma.browser.SqliteMaster;

@ORMMapping(
        dbname = "local_copy",
        yyyyMMddHHmmss = "20110521000000",
        ORMClasses = {
                SqliteMaster.class,
                Configuration.class,
                Recent.class
                })
public class FandroidContentProvider extends AbstractORMContentProvider {
    public static AnnotatedORMDefinition ormDefinition = new AnnotatedORMDefinition(FandroidContentProvider.class);
    public static AnnotatedORM orm = new AnnotatedORM(ormDefinition);
}
