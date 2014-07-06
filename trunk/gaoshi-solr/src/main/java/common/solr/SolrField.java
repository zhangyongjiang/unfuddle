package common.solr;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@Target(FIELD)
public @interface SolrField {
	boolean stored() default true;

	boolean indexed() default true;

	boolean sortable() default false;

	boolean text() default true;
}
