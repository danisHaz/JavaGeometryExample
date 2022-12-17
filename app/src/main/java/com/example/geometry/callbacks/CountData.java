package com.example.geometry.callbacks;

import com.example.geometry.utils.CallbackData;

public class CountData implements CallbackData {

    private int position;
    private boolean type;

    public CountData(int position, boolean type) {
        this.position = position;
        this.type = type;
    }

    public int getFirst() {
        return position;
    }

    public boolean getType() {
        return type;
    }
}
