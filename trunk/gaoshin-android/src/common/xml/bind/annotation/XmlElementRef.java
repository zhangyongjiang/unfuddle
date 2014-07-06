/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package common.xml.bind.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({FIELD,METHOD})
public @interface XmlElementRef {
    /**
     * The Java type being referenced.
     * <p>
     * If the value is DEFAULT.class, the type is inferred from the
     * the type of the JavaBean property.
     */
    Class type() default DEFAULT.class;

    /**
     * This parameter and {@link #name()} are used to determine the
     * XML element for the JavaBean property.
     *
     * <p> If <tt>type()</tt> is <tt>JAXBElement.class</tt> , then 
     * <tt>namespace()</tt> and <tt>name()</tt>
     * point to a factory method with {@link XmlElementDecl}. The XML
     * element name is the element name from the factory method's
     * {@link XmlElementDecl} annotation or if an element from its
     * substitution group (of which it is a head element) has been
     * substituted in the XML document, then the element name is from the
     * {@link XmlElementDecl} on the substituted element.
     *
     * <p> If {@link #type()} is not <tt>JAXBElement.class</tt>, then
     * the XML element name is the XML element name statically
     * associated with the type using the annotation {@link
     * XmlRootElement} on the type. If the type is not annotated with
     * an {@link XmlElementDecl}, then it is an error.
     *
     * <p> If <tt>type()</tt> is not <tt>JAXBElement.class</tt>, then
     * this value must be "". 
     *
     */
    String namespace() default "";
    /**
     *
     * @see #namespace()
     */
    String name() default "##default";

    /**
     * Used in {@link XmlElementRef#type()} to
     * signal that the type be inferred from the signature
     * of the property.
     */
    static final class DEFAULT {}
}
