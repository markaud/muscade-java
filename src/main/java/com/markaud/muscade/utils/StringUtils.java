package com.markaud.muscade.utils;

public class StringUtils {
    public static String performString(String string) {
        return string == null ? "" : string;
    }

    public static String performString(String string, String defaultValue) {
        return string == null || string.trim().isEmpty() ? defaultValue : string;
    }

    public static String performString(Integer integer) {
        return integer == null ? "" : integer.toString();
    }
}
