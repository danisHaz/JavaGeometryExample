package com.example.common_utils.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;

import app.IShape;
import app.Segment;

public final class SegmentDrawer extends Drawer {

    private static SegmentDrawer drawer = null;
    private final String className = Segment.class.getName();

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void draw(IShape shape, Canvas canvas, Paint paint) throws Exception {
        Segment segment = (Segment) shape;
        canvas.drawLine((float)(segment.getStart().getX(0) + centerX), (float)(-segment.getStart().getX(1) + centerY),
                (float)(segment.getFinish().getX(0) + centerX), (float)(-segment.getFinish().getX(1) + centerY), paint);
    }

    public static SegmentDrawer createInstance() {
        if (drawer == null)
            drawer = new SegmentDrawer();

        return drawer;
    }
}
