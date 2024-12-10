package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionAccessor {
    protected final Object target;
    protected final Class<?> clazz;

    public ReflectionAccessor(Object target) {
        this(target, target.getClass());
    }

    public ReflectionAccessor(Object target, Class<?> clazz) {
        this.target = target;
        this.clazz = clazz;
    }

    public Field getField(final String fieldName) {
        return getField(clazz, fieldName);
    }

    public Field getField(final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
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

    public void setFieldValue(final Class<?> clazz, final String fieldName, final Object value) {
        try {
            getField(clazz, fieldName).set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod(final String methodName, final Class<?>... parameterTypes) {
        try {
            final Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
