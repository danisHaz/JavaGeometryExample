package com.example.common_utils.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;

import app.IShape;

public abstract class Drawer {

    protected static double centerX = 200;
    protected static double centerY = 200;

    public static void setCenter(double centerX, double centerY) {
        Drawer.centerX = centerX;
        Drawer.centerY = centerY;
    }

    public static double getCenterX() {
        return centerX;
    }

    public static double getCenterY() {
        return centerY;
    }

    public abstract String getClassName();

    public abstract void draw(IShape shape, Canvas canvas, Paint paint) throws Exception;
}
