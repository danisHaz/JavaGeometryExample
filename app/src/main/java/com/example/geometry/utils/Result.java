package com.example.geometry.utils;

public class Result<T> {
    private boolean error;
    private T data = null;

    public Result(boolean error, T data) {
        this.error = error;
        this.data = data;
    }

    public boolean getError() {
        return error;
    }

    public T getData() {
        return data;
    }
}
