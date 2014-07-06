package org.gaoshin.openflier.util;

import java.lang.reflect.Field;

public interface FieldFoundCallback {
    void field(Object o, Field field) throws Exception;
}
