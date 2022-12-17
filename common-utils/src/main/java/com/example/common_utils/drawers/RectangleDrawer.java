package com.example.common_utils.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import app.IShape;
import app.Rectangle;

public final class RectangleDrawer extends Drawer {

    private static RectangleDrawer drawer = null;
    private final String className = Rectangle.class.getName();

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void draw(IShape shape, Canvas canvas, Paint paint) throws Exception {
        Rectangle rectangle = (Rectangle) shape;
        int n = rectangle.getN();
        float[] xcoords = new float[n];
		float[] ycoords = new float[n];
        Path path = new Path();
        path.moveTo(xcoords[0] + (float)centerX, -ycoords[0] + (float)centerY);
		for (int i = 0; i < n; i++) {
			xcoords[i] = (float)(rectangle.getP()[i].getX(0) + centerX);
			ycoords[i] = (float)(-rectangle.getP()[i].getX(1) + centerY);
            path.lineTo(xcoords[i], ycoords[i]);
		}
        path.lineTo(xcoords[0], ycoords[0]);

		canvas.drawPath(path, paint);
    }

    public static RectangleDrawer createInstance() {
        if (drawer == null)
            drawer = new RectangleDrawer();

        return drawer;
    }
}

