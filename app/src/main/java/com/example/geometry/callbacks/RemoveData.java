package com.example.geometry.callbacks;

import com.example.geometry.utils.CallbackData;

public class RemoveData implements CallbackData {

    private int position;

    public RemoveData(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
