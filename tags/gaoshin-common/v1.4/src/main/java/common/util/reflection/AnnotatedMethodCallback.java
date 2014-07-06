package common.util.reflection;

import java.lang.reflect.Method;

public interface AnnotatedMethodCallback {
    void method(Object obj, Method method);
}
