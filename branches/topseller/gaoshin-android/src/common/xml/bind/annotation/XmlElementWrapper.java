package common.xml.bind.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) @Target({FIELD, METHOD})
public @interface XmlElementWrapper {
    /**
     * Name of the XML wrapper element. By default, the XML wrapper
     * element name is derived from the JavaBean property name.
     */
    String name() default "##default";

    /**
     * XML target namespace of the XML wrapper element.
     * <p>
     * If the value is "##default", then the namespace is determined
     * as follows:
     * <ol>
     *  <li>
     *  If the enclosing package has {@link XmlSchema} annotation,
     *  and its {@link XmlSchema#elementFormDefault() elementFormDefault}
     *  is {@link XmlNsForm#QUALIFIED QUALIFIED}, then the namespace of
     *  the enclosing class.
     *
     *  <li>
     *  Otherwise "" (which produces unqualified element in the default
     *  namespace.
     * </ol>
     */
    String namespace() default "##default";

    /**
     * If true, the absence of the collection is represented by
     * using <tt>xsi:nil='true'</tt>. Otherwise, it is represented by
     * the absence of the element.
     */
    boolean nillable() default false;

    /**
     * Customize the wrapper element declaration to be required.
     *
     * <p>
     * If required() is true, then the corresponding generated
     * XML schema element declaration will have <tt>minOccurs="1"</tt>,
     * to indicate that the wrapper element is always expected.
     *
     * <p>
     * Note that this only affects the schema generation, and
     * not the unmarshalling or marshalling capability. This is
     * simply a mechanism to let users express their application constraints
     * better.
     *
     * @since JAXB 2.1
     */
    boolean required() default false;
}
