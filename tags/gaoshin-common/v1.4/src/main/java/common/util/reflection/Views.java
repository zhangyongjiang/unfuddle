package common.util.reflection;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface Views {
    public static final Object ANY = Object.class;

    Class[] value() default {};

    Class[] excludes() default {};
}
