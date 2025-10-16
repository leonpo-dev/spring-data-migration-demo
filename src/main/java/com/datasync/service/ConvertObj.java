package com.datasync.service;

/**
 * @author szy
 * @version 1.0
 * @date 2023-06-13 16:38:32
 * @description 类型转换, 默认缺省值为 null
 */
public interface ConvertObj {

    default String toStr(Object obj) {
        if (obj == null) return null;

        try {
            return String.valueOf(obj);
        } catch (Exception e) {
            return null;
        }
    }

    default String toStr(Object obj, String defaultValue) {
        if (obj == null) return defaultValue;
        try {
            return String.valueOf(obj);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    default Long toLong(Object obj) {
        if (obj == null) return null;
        try {
            return Long.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return null;
        }
    }

    default Long toLong(Object obj, Long defaultValue) {
        if (obj == null) return defaultValue;
        try {
            return Long.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    default Integer toInt(Object obj) {
        if (obj == null) return null;
        try {
            return Integer.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return null;
        }
    }

    default Integer toInt(Object obj, Integer defaultValue) {
        if (obj == null) return defaultValue;
        try {
            return Integer.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    default Boolean toBoolean(Object obj) {
        if (obj == null) return null;
        try {
            if (obj instanceof Number) {
                return ((Number) obj).intValue() != 0;
            }
            return Boolean.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return null;
        }
    }

    default Boolean toBoolean(Object obj, Boolean defaultValue) {
        if (obj == null) return defaultValue;
        try {
            if (obj instanceof Number) {
                return ((Number) obj).intValue() != 0;
            }
            return Boolean.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return defaultValue;
        }
    }


}
