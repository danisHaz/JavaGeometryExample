package com.example.common_utils.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import app.IShape;
import app.Trapeze;

public final class TrapezeDrawer extends Drawer {

    private static TrapezeDrawer drawer = null;
    private final String className = Trapeze.class.getName();

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void draw(IShape shape, Canvas canvas, Paint paint) throws Exception {
        Trapeze trapeze = (Trapeze) shape;
        int n = trapeze.getN();
        float[] xcoords = new float[n];
		float[] ycoords = new float[n];
        Path path = new Path();
        path.moveTo(xcoords[0] + (float)centerX, -ycoords[0] + (float)centerY);
		for (int i = 0; i < n; i++) {
			xcoords[i] = (float)(trapeze.getP()[i].getX(0) + centerX);
			ycoords[i] = (float)(-trapeze.getP()[i].getX(1) + centerY);
            path.lineTo(xcoords[i], ycoords[i]);
		}
        path.lineTo(xcoords[0], ycoords[0]);

		canvas.drawPath(path, paint);
    }

    public static TrapezeDrawer createInstance() {
        if (drawer == null)
            drawer = new TrapezeDrawer();

        return drawer;
    }
}
