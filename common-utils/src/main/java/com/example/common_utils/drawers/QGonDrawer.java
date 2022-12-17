package com.example.common_utils.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import app.IShape;
import app.QGon;

public final class QGonDrawer extends Drawer {

    private static QGonDrawer drawer = null;
    private final String className = QGon.class.getName();

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void draw(IShape shape, Canvas canvas, Paint paint) throws Exception {
        QGon qgon = (QGon) shape;
        int n = qgon.getN();
        float[] xcoords = new float[n];
		float[] ycoords = new float[n];
        Path path = new Path();
        path.moveTo(xcoords[0] + (float)centerX, -ycoords[0] + (float)centerY);
		for (int i = 0; i < n; i++) {
			xcoords[i] = (float)(qgon.getP()[i].getX(0) + centerX);
			ycoords[i] = (float)(-qgon.getP()[i].getX(1) + centerY);
            path.lineTo(xcoords[i], ycoords[i]);
		}
        path.lineTo(xcoords[0], ycoords[0]);

		canvas.drawPath(path, paint);
    }


    public static QGonDrawer createInstance() {
        if (drawer == null)
            drawer = new QGonDrawer();

        return drawer;
    }
}
