package com.gaoshin.top;

import com.gaoshin.sorma.AnnotatedORM;
import com.gaoshin.sorma.annotation.AbstractORMContentProvider;
import com.gaoshin.sorma.annotation.AnnotatedORMDefinition;
import com.gaoshin.sorma.annotation.ORMMapping;
import com.gaoshin.sorma.browser.SqliteMaster;

@ORMMapping(
        dbname = "local_copy",
        yyyyMMddHHmmss = "20110623000000",
        ORMClasses = {
                SqliteMaster.class,
                Shortcut.class,
                Configuration.class,
                ShortcutGroup.class,
                Job.class,
                JobExecution.class,
                })
public class TopContentProvider extends AbstractORMContentProvider {
    public static AnnotatedORMDefinition ormDefinition = new AnnotatedORMDefinition(TopContentProvider.class);
    public static AnnotatedORM orm = new AnnotatedORM(ormDefinition);
}
