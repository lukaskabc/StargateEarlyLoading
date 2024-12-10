package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import java.lang.reflect.Field;

public class ReflectionAccessor {
    private final Object target;

    public ReflectionAccessor(Object target) {
        this.target = target;
    }

    public Field getField(final String fieldName) {
        try {
            final Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getFieldValue(final String fieldName) {
        try {
            return getField(fieldName).get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFieldValue(final String fieldName, final Object value) {
        try {
            getField(fieldName).set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
