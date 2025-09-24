package com.signlearn.util;

public class Result<T> {
    private final boolean success;
    private final T value;
    private final String error;

    private Result(boolean success, T value, String error) {
        this.success = success;
        this.value = value;
        this.error = error;
    }

    // Newer style
    public static <T> Result<T> success(T value) {
        return new Result<>(true, value, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(false, null, message);
    }

    public static <T> Result<T> failure(String message) {
        return error(message);
    }

    // Backwards compatibility aliases
    public static <T> Result<T> ok(T value) {
        return success(value);
    }

    public static <T> Result<T> error(String message, T value) {
        return new Result<>(false, value, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }
}
