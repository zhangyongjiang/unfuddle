package org.gaoshin.openflier.util;

import java.lang.reflect.Field;

public class AnnotatedField<T> implements AnnotatedFieldCallback {
    private Object object;
    private Field field;
    @Override
    public void field(Object object, Field field) {
        this.field = field;
        this.object = object;
        field.setAccessible(true);
    }
    public T getValue() throws Exception {
        return (T)field.get(object);
    }
    public void setValue(T value) throws Exception {
        field.set(object, value);
    }
    
    public void setObject(Object object){
        this.object = object;
    }
}
