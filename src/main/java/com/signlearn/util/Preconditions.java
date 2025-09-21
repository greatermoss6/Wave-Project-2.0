package com.signlearn.util;

public class Preconditions {
    public static void check(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}