package com.example.common_utils.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;

import app.IShape;
import app.Polyline;

public final class PolylineDrawer extends Drawer {

    private static PolylineDrawer drawer = null;
    private final String className = Polyline.class.getName();

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void draw(IShape shape, Canvas canvas, Paint paint) throws Exception {
        Polyline polyline = (Polyline) shape;
        int n = polyline.getN();
        for (int i = 1; i < n; i++) {
			canvas.drawLine((float)(polyline.getP()[i - 1].getX(0) + centerX), (float)(-polyline.getP()[i - 1].getX(1) + centerY),
                    (float)(polyline.getP()[i].getX(0) + centerX), (float)(-polyline.getP()[i].getX(1) + centerY), paint);
        }
    }

    public static PolylineDrawer createInstance() {
        if (drawer == null)
            drawer = new PolylineDrawer();

        return drawer;
    }
}

