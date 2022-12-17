package com.example.geometry.callbacks;

import com.example.geometry.utils.CallbackData;

import java.util.List;

public class DragFigureData implements CallbackData {
    private final int index;
    private final String type;
    private final List<Double> coords;

    public DragFigureData(int index, String type, List<Double> coords) {
        this.index = index;
        this.type = type;
        this.coords = coords;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public List<Double> getCoords() {
        return coords;
    }
}
