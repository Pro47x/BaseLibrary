package com.minicart.android.baselibrary.support;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.BiPredicate;

/**
 * @类名：ObjectUtil
 * @描述：
 * @创建人：54506
 * @创建时间：2017/01/03 23:38
 * @版本：
 */
public final class ObjectUtil {

    private ObjectUtil() {
        throw new IllegalStateException("No instances!");
    }

    /**
     * 验证对象是否为null，如果是null就抛出NullPointerException
     * 使用给定的message
     *
     * @param <T>     要验证的对象的类型
     * @param object  要验证的对象
     * @param message 当验证的对象为null时指定的message
     * @return 对象本身
     * @throws NullPointerException 如果这个对象为null
     */
    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * Compares two potentially null objects with each other using Object.equals.
     *
     * @param o1 the first object
     * @param o2 the second object
     * @return the comparison result
     */
    public static boolean equals(Object o1, Object o2) { // NOPMD
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    /**
     * 返回不是null对象的hashCode，如果是null对象就返回0.
     *
     * @param o 需要得到hashCode的对象.
     * @return 对象的hashCode
     */
    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    /**
     * Compares two integer values similar to Integer.compare.
     *
     * @param v1 第一个值
     * @param v2 第二个值
     * @return 比较的结果
     */
    public static int compare(int v1, int v2) {
        return v1 < v2 ? -1 : (v1 > v2 ? 1 : 0);
    }

    /**
     * Compares two integer values similar to Long.compare.
     *
     * @param v1 the first value
     * @param v2 the second value
     * @return the comparison result
     */
    public static int compare(long v1, long v2) {
        return v1 < v2 ? -1 : (v1 > v2 ? 1 : 0);
    }

    static final BiPredicate<Object, Object> EQUALS = new BiPredicate<Object, Object>() {
        @Override
        public boolean test(Object o1, Object o2) {
            return ObjectUtil.equals(o1, o2);
        }
    };

    /**
     * Returns a BiPredicate that compares its parameters via Objects.equals().
     *
     * @param <T> the value type
     * @return the bi-predicate instance
     */
    @SuppressWarnings("unchecked")
    public static <T> BiPredicate<T, T> equalsPredicate() {
        return (BiPredicate<T, T>) EQUALS;
    }

    /**
     * Validate that the given value is positive or report an IllegalArgumentException with
     * the parameter name.
     *
     * @param value     the value to validate
     * @param paramName the parameter name of the value
     * @return value
     * @throws IllegalArgumentException if bufferSize &lt;= 0
     */
    public static int verifyPositive(int value, String paramName) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
        }
        return value;
    }

    /**
     * Validate that the given value is positive or report an IllegalArgumentException with
     * the parameter name.
     *
     * @param value     the value to validate
     * @param paramName the parameter name of the value
     * @return value
     * @throws IllegalArgumentException if bufferSize &lt;= 0
     */
    public static long verifyPositive(long value, String paramName) {
        if (value <= 0L) {
            throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
        }
        return value;
    }

    /**
     * @param list
     * @return null--->false
     */
    public static boolean nonNull(List list) {
        return list != null && !list.isEmpty();
    }

    /**
     * @param map
     * @return null--->false
     */
    public static boolean nonNull(Map map) {
        return map != null && !map.isEmpty();
    }

}