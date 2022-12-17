package com.example.common_utils.drawers;

import app.IShape;

import java.util.ArrayList;
import java.util.Objects;

public class DrawerFactory {
    public static Drawer create(IShape shape) throws Exception {
        for (Drawer drawer: drawerDelegates) {
            if (Objects.equals(drawer.getClassName(), shape.getClass().getName())) {
                return drawer;
            }
        }

        throw new Exception("Appropriate drawer is not implemented");
    }

    private static final ArrayList<Drawer> drawerDelegates = new ArrayList<Drawer>() {{
        add(CircleDrawer.createInstance());
        add(NGonDrawer.createInstance());
        add(PolylineDrawer.createInstance());
        add(QGonDrawer.createInstance());
        add(RectangleDrawer.createInstance());
        add(SegmentDrawer.createInstance());
        add(TGonDrawer.createInstance());
        add(TrapezeDrawer.createInstance());
    }};
}
