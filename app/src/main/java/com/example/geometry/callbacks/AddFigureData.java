package com.example.geometry.callbacks;

import com.example.geometry.utils.CallbackData;

import java.util.List;

public class AddFigureData implements CallbackData {
    private String type;
    private List<Double> coords;

    public AddFigureData(String type, List<Double> coords) {
        this.type = type;
        this.coords = coords;
    }

    public String getType() {
        return type;
    }

    public List<Double> getCoords() {
        return coords;
    }
}
