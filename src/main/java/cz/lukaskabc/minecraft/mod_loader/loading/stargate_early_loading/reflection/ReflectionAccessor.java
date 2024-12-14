package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import org.jspecify.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Allows reflection access to private fields and methods.
 */
public class ReflectionAccessor {
    protected final Object target;
    protected final Class<?> clazz;

    /**
     * Initializes the accessor with the target object and its class.
     *
     * @param target the object to access
     * @see Object#getClass()
     */
    public ReflectionAccessor(Object target) {
        this(target, target.getClass());
    }

    /**
     * Initializes the accessor with the target object and the class.
     *
     * @param target the object to access
     * @param clazz  the class of the object where the fields and methods are located
     */
    public ReflectionAccessor(Object target, Class<?> clazz) {
        this.target = target;
        this.clazz = clazz;
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
        try {
            final Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... parameterTypes) {
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Gets the value of the field with the given name.
     *
     * @param clazz     the class where the field is located
     * @param target    the object to access
     * @param fieldName the name of the field to access
     * @return the value of the field
     * @throws ReflectionException if the field does not exist or other reflection error occurs
     */
    public static Object getFieldValue(final Class<?> clazz, @Nullable Object target, final String fieldName) {
        try {
            return getField(clazz, fieldName).get(target);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Gets the field with the given name from the given class.
     *
     * @param clazz     the class where the field is located
     * @param fieldName the name of the field to access
     * @return the field
     * @throws ReflectionException if the field does not exist or other reflection error occurs
     */
    public static Field getField(final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Sets the value of the field with the given name.
     *
     * @param clazz     the class where the field is located
     * @param fieldName the name of the field to set
     * @param value     the value to set
     * @throws ReflectionException if the field does not exist or other reflection error occurs
     */
    public void setFieldValue(final Class<?> clazz, final String fieldName, final Object value) {
        try {
            getField(clazz, fieldName).set(target, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Gets the field with the given name.
     *
     * @param fieldName the name of the field to access
     * @return the field
     * @throws ReflectionException if the field does not exist or other reflection error occurs
     */
    public Field getField(final String fieldName) {
        return getField(clazz, fieldName);
    }

    /**
     * Sets the value of the field with the given name.
     *
     * @param fieldName the name of the field to set
     * @param value     the value to set
     * @throws ReflectionException if the field does not exist or other reflection error occurs
     */
    public void setFieldValue(final String fieldName, final Object value) {
        try {
            getField(fieldName).set(target, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Gets the method with the given name and parameter types.
     *
     * @param methodName     the name of the method to access
     * @param parameterTypes the parameter types of the method
     * @return the method
     * @throws ReflectionException if the method does not exist or other reflection error occurs
     */
    public Method getMethod(final String methodName, final Class<?>... parameterTypes) {
        return getMethod(clazz, methodName, parameterTypes);
    }

}
