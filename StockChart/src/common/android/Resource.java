package common.android;

import java.lang.reflect.Field;

import android.content.Context;

public class Resource {
    public static int getResource(Context context, String type, String name) {
        try {
            String pkg = context.getApplicationInfo().packageName;
            Class rcls = Class.forName(pkg + ".R");
            Class[] classes = rcls.getClasses();
            for(Class cls : classes) {
                if(cls.getSimpleName().equals(type)) {
                    Field field = cls.getField(name);
                    return ((Integer)field.get(null));
                }
            }
            throw new RuntimeException("not found " + pkg + ".R." + type);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static int drawable(Context context, String name) {
        return getResource(context, "drawable", name);
    }
    
    public static int id(Context context, String name) {
        return getResource(context, "id", name);
    }
    
    public static int layout(Context context, String name) {
        return getResource(context, "layout", name);
    }
    
    public static int menu(Context context, String name) {
        return getResource(context, "menu", name);
    }
    
    public static int string(Context context, String name) {
        return getResource(context, "string", name);
    }
}
