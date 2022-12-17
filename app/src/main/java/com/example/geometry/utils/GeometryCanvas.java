package com.example.geometry.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.common_utils.deserializers.IFigureFactory;
import com.example.common_utils.drawers.Drawer;
import com.example.common_utils.drawers.DrawerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import app.IShape;

public class GeometryCanvas extends View {

    private ArrayList<IShape> list;
    private ArrayList<IShape> redList;
    private Paint paint;

    private void init() {
        list = new ArrayList<>();
        redList = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }

    public GeometryCanvas(Context context) {
        super(context);
        init();
    }

    public GeometryCanvas(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public GeometryCanvas(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Log.e("kek", Integer.toString(list.size()));
        return new GeometryCanvasData(superState, list, redList);
    }

    @Override
    public void onRestoreInstanceState(Parcelable p) {
        GeometryCanvasData data = (GeometryCanvasData) p;
        super.onRestoreInstanceState(data.getSuperState());

        list = data.getShapes();
        redList = data.getRedShapes();

        while (!OperationStorage.eventQueue.isEmpty()) {
            OperationStorage.Operation<?> operation = OperationStorage.eventQueue.poll();
            assert operation != null;
            if (operation.type == OperationStorage.Operation.OperationType.ADD) {
                mAddToListAndDraw((IShape)operation.shape);
            } else if (operation.type == OperationStorage.Operation.OperationType.REMOVE) {
                mRemoveAll();
            } else if (operation.type == OperationStorage.Operation.OperationType.CLEAR) {
                mClearAndDrawAll((List<IShape>)operation.shape);
            } else if (operation.type == OperationStorage.Operation.OperationType.REMOVE_ONE) {
                mRemove(operation.position);
            }
        }

        Log.e("lol", Integer.toString(list.size()));
    }

    public List<IShape> getList() {
        return list;
    }

    public void clearAndDrawAll(List<IShape> redList, boolean redrawNow) {
        if (redrawNow) {
            mClearAndDrawAll(redList);
            invalidate();
        } else {
            OperationStorage.eventQueue.add(new OperationStorage.Operation<>(
                    OperationStorage.Operation.OperationType.CLEAR,
                    redList
            ));
        }
    }

    public void removeAll(boolean redrawNow) {
        if (redrawNow) {
            mRemoveAll();
            invalidate();
        } else {
            OperationStorage.eventQueue.add(new OperationStorage.Operation<>(
                    OperationStorage.Operation.OperationType.REMOVE,
                    null
            ));
        }
    }

    public void addToListAndDraw(IShape shape, boolean redrawNow) {
        if (redrawNow) {
            mAddToListAndDraw(shape);
            invalidate();
        } else {
            OperationStorage.eventQueue.add(new OperationStorage.Operation<>(
                    OperationStorage.Operation.OperationType.ADD,
                    shape
            ));
        }
    }

    public void remove(int position) {
        OperationStorage.eventQueue.add(new OperationStorage.Operation<>(
                OperationStorage.Operation.OperationType.REMOVE_ONE,
                position
        ));
    }

    private void mClearAndDrawAll(List<IShape> redList) {
        this.redList.clear();
        if (redList != null)
            this.redList.addAll(redList);
    }

    private void mRemoveAll() {
        redList.clear();
        list.clear();
    }

    private void mAddToListAndDraw(IShape shape) {
        list.add(shape);
        Log.e("tut", Integer.toString(list.size()));
    }

    private void mUpdate(int pos, IShape shape) {
        list.remove(pos);
        list.add(shape);
    }

    private void mRemove(int position) {
        list.remove(position);
    }

    private boolean findIn(IShape shape, List<IShape> shapeList) {
        if (shapeList == null || shape == null)
            return false;

        for (IShape curShape : shapeList) {
            if (shape.toString().equals(curShape.toString()))
                return true;
        }

        return false;
    }

    private void drawAxes(@NonNull Canvas canvas, Paint paint) throws Exception {
        canvas.drawLine(
                0,
                (float) canvas.getHeight() / 2,
                (float) canvas.getWidth(),
                (float) canvas.getHeight() / 2,
                paint
        );
        canvas.drawLine(
                (float) canvas.getWidth() / 2,
                (float) canvas.getHeight(),
                (float) canvas.getWidth() / 2,
                0,
                paint
        );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Drawer.setCenter((double)getMeasuredWidth() / 2, (double)getMeasuredHeight() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(getClass().toString(), Integer.toString(list.size()));
        try {
            drawAxes(canvas, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        list.forEach((shape) -> {
            try {
                if (findIn(shape, redList)) {
                    paint.setColor(Color.RED);
                    DrawerFactory.create(shape).draw(shape, canvas, paint);
                    paint.setColor(Color.GRAY);
                } else {
                    DrawerFactory.create(shape).draw(shape, canvas, paint);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    static final class GeometryCanvasData extends View.BaseSavedState {

        private final ArrayList<String> shapes;
        private final ArrayList<String> redShapes;

        public GeometryCanvasData(Parcelable superState, ArrayList<IShape> shapes, ArrayList<IShape> redShapes) {
            super(superState);
            this.shapes = new ArrayList<>();
            this.redShapes = new ArrayList<>();
            shapes.forEach((shape) -> this.shapes.add(shape.toString()));
            redShapes.forEach((shape) -> this.redShapes.add(shape.toString()));
        }

        public GeometryCanvasData(Parcel in) {
            super(in);
            shapes = new ArrayList<>();
            redShapes = new ArrayList<>();
            in.readStringList(shapes);
            in.readStringList(redShapes);
        }

        public ArrayList<IShape> getShapes() {
            ArrayList<IShape> resultShapes = new ArrayList<>();
            shapes.forEach((shape) -> {
                resultShapes.add(IFigureFactory.create(shape));
            });

            return resultShapes;
        }

        public ArrayList<IShape> getRedShapes() {
            ArrayList<IShape> resultShapes = new ArrayList<>();
            redShapes.forEach((shape) -> {
                resultShapes.add(IFigureFactory.create(shape));
            });

            return resultShapes;
        }

        public static final Creator<GeometryCanvasData> CREATOR = new Creator<>() {
            @Override
            public GeometryCanvasData createFromParcel(Parcel in) {
                return new GeometryCanvasData(in);
            }

            @Override
            public GeometryCanvasData[] newArray(int size) {
                return new GeometryCanvasData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeStringList(shapes);
            parcel.writeStringList(redShapes);
        }
    }

    private static final class OperationStorage {
        public static Queue<Operation<?>> eventQueue = new LinkedList<>();

        public static final class Operation<T> {
            private OperationType type;
            private T shape;
            private int position = -1;

            public Operation(OperationType type, T shape) {
                this.type = type;
                this.shape = shape;
            }

            public Operation(OperationType type, int position) {
                this(type, null);
                this.position = position;
            }

            public enum OperationType {
                REMOVE,
                ADD,
                CLEAR,
                REMOVE_ONE,
            }
        }
    }
}
