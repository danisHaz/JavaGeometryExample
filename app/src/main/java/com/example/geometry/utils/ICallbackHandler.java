package com.example.geometry.utils;

public interface ICallbackHandler<T extends CallbackData> {
    void handleEvent(T data);
}
