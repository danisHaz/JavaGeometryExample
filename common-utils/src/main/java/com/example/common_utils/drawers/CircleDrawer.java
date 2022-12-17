package com.example.common_utils.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;

import app.Circle;
import app.IShape;

public final class CircleDrawer extends Drawer {

    private static CircleDrawer drawer = null;
    private final String className = Circle.class.getName();

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void draw(IShape shape, Canvas canvas, Paint paint) throws Exception {
        Circle circle = (Circle) shape;
		canvas.drawCircle(
                (float)(circle.getP().getX(0) + centerX),
                (float)(-circle.getP().getX(1) + centerY),
                (float)(circle.getR()),
                paint
        );
    }

    public static CircleDrawer createInstance() {
        if (drawer == null)
            drawer = new CircleDrawer();

        return drawer;
    }
}
