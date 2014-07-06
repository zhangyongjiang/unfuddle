package common.util.reflection;

import java.lang.reflect.Field;

public interface FieldFoundCallback {
    void field(Object o, Field field) throws Exception;
}
