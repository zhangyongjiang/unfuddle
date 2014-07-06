package org.gaoshin.openflier.util;

import java.lang.reflect.Method;

public interface AnnotatedMethodCallback {
    void method(Object obj, Method method) throws Exception;
}
