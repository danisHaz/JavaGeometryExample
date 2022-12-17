package com.example.geometry.callbacks;

import com.example.geometry.utils.CallbackData;

public class CheckIfCrossData implements CallbackData {

    private int first;
    private int second;

    public CheckIfCrossData(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }
}
