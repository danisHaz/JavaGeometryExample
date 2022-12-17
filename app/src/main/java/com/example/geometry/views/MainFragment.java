package com.example.geometry.views;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.common_utils.deserializers.IFigureFactory;
import com.example.common_utils.repository.MongoDb;
import com.example.geometry.R;
import com.example.geometry.callbacks.AddFigureData;
import com.example.geometry.callbacks.CheckIfCrossData;
import com.example.geometry.callbacks.CountData;
import com.example.geometry.callbacks.DragFigureData;
import com.example.geometry.callbacks.RemoveData;
import com.example.geometry.utils.GeometryCanvas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.Circle;
import app.IShape;
import app.NGon;
import app.Point2D;
import app.Polyline;
import app.QGon;
import app.Rectangle;
import app.Segment;
import app.TGon;
import app.Trapeze;

public class MainFragment extends Fragment {

    private static final String fileName = "./src/main/java/ui/figures.txt";
    private static final String pngFilePath = "./src/main/java/ui/snapshot.png";

    public List<IShape> list = new ArrayList<>();
    public List<String> types = new ArrayList<>();

    private Button addFigure = null;
    private Button removeFigure = null;
    private Button saveAsImg = null;
    private Button saveToFile = null;
    private Button computeS = null;
    private Button computeP = null;
    private Button dragFigure = null;
    private Button addToDB = null;
    private Button retrieveFromDB = null;
    private Button clear = null;
    private Button uploadFromFile = null;
    private Button checkIfCross = null;

    private GeometryCanvas canvas = null;
    private TextView answer = null;

    private MongoDb database = null;

    ActivityResultLauncher<String[]> readFileLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            this::readFromDocument
    );

    ActivityResultLauncher<String[]> writeToFileLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            this::writeToDocument
    );

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = MongoDb.create();

        canvas = view.findViewById(R.id.geometry);
        answer = view.findViewById(R.id.resultField);

        addFigure = view.findViewById(R.id.addFigure);
        addFigure.setOnClickListener(this::onAddFigure);

        removeFigure = view.findViewById(R.id.removeFigure);
        removeFigure.setOnClickListener(this::onRemoveFigure);

        saveAsImg = view.findViewById(R.id.saveAsImg);
        saveAsImg.setOnClickListener(this::onSaveAsImg);

        saveToFile = view.findViewById(R.id.saveToFile);
        saveToFile.setOnClickListener(this::writeToFile);

        computeS = view.findViewById(R.id.computeS);
        computeS.setOnClickListener(this::onComputeS);

        computeP = view.findViewById(R.id.computeP);
        computeP.setOnClickListener(this::onComputeP);

        dragFigure = view.findViewById(R.id.dragFigure);
        dragFigure.setOnClickListener(this::onDragFigure);

        addToDB = view.findViewById(R.id.addToDB);
        addToDB.setOnClickListener(this::onAddToDatabase);

        retrieveFromDB = view.findViewById(R.id.retrieveFromDB);
        retrieveFromDB.setOnClickListener(this::onRetrieveFromDatabase);

        clear = view.findViewById(R.id.clear);
        clear.setOnClickListener(this::onInitialize);

        uploadFromFile = view.findViewById(R.id.uploadFromFile);
        uploadFromFile.setOnClickListener(this::onUploadFromFile);

        checkIfCross = view.findViewById(R.id.checkIfCross);
        checkIfCross.setOnClickListener(this::onCheckIfCross);
    }

    public static void showAlert(Context context, String alert) {
        Toast.makeText(context, alert, Toast.LENGTH_SHORT).show();
    }

    // callback func
    public void moveFigure(DragFigureData data) {
        int pos = data.getIndex();
        String type = data.getType().trim();
        Double[] args = data.getCoords().toArray(new Double[0]);

        IShape shape = canvas.getList().get(pos);
        canvas.remove(pos);

        try {
            switch (type) {
                case "Rot":
                    shape.rot(args[0]);
                    break;
                case "SymAxis":
                    shape.symAxis(args[0].intValue());
                    break;
                case "Shift":
                    shape.shift(new Point2D(args[0], args[1]));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        canvas.addToListAndDraw(shape, false);
    }

    // callback func
    public void countByPos(CountData data) {
        int pos = data.getFirst();
        boolean type = data.getType();
        canvas.addToListAndDraw(list.get(pos), false);

        answer.setVisibility(View.VISIBLE);
        double res = 0.0;
        try {
            if (type)
                res = list.get(pos).length();
            else
                res = list.get(pos).square();
        } catch (Exception e) {
            e.printStackTrace();
        }

        answer.setText(String.valueOf(res));
    }

    // callback func
    public void setIfCross(CheckIfCrossData data) {
        int firstPosition = data.getFirst();
        int secondPosition = data.getSecond();

        try {
            answer.setVisibility(View.VISIBLE);
            if (canvas.getList().get(firstPosition).cross(canvas.getList().get(secondPosition)))
                answer.setText("Cross");
            else
                answer.setText("Not Cross");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // callback func
    public void removeFigureByPosition(RemoveData data) {
        int pos = data.getPosition();
        canvas.remove(pos);
        canvas.clearAndDrawAll(null, false);
    }

    public void readFromDocument(Uri uri) {
        InputStream fstream;
        try {
            fstream = requireContext().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        try {
            String line;
            int lineInd = 0;
            while ((line = br.readLine()) != null) {
                Log.e("data", line);
                IShape restoredShape = IFigureFactory.create(line);
                if (restoredShape != null) {
                    canvas.addToListAndDraw(restoredShape, true);
                } else {
                    System.out.println(String.format("Shape on line %d is invalid, ignoring it", lineInd));
                }
                lineInd++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            br.close();
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToDocument(Uri uri) {
        try {
            OutputStream stream = getContext().getContentResolver().openOutputStream(uri, "w");
            String endOf = "\n";
            for (int i = 0; i < canvas.getList().size(); i++) {
                if (i == canvas.getList().size() - 1)
                    endOf = "";
                stream.write(
                        String.format("%s%s", canvas.getList().get(i).toString(), endOf)
                                .getBytes(StandardCharsets.UTF_8));
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCircle(double[] coords) throws Exception {
        double r = coords[2];
        Point2D point = new Point2D(coords[0], coords[1]);
        canvas.addToListAndDraw(new Circle(point, r), false);
    }

    public void addNGon(double[] coords) throws Exception {
        int n = coords.length / 2;
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < coords.length; i += 2) {
            points[i / 2] = new Point2D(coords[i], coords[i + 1]);
        }

        canvas.addToListAndDraw(new NGon(points), false);
    }

    public void addQGon(double[] coords) throws Exception {
        Point2D[] points = new Point2D[4];
        for (int i = 0; i < coords.length; i += 2) {
            points[i / 2] = new Point2D(coords[i], coords[i + 1]);
        }
        canvas.addToListAndDraw(new QGon(points), false);
    }

    public void addRectangle(double[] coords) throws Exception {
        Point2D[] points = new Point2D[4];
        for (int i = 0; i < coords.length; i += 2) {
            points[i / 2] = new Point2D(coords[i], coords[i + 1]);
        }
        canvas.addToListAndDraw(new Rectangle(points), false);
    }

    public void addTrapeze(double[] coords) throws Exception {
        Point2D[] points = new Point2D[4];
        for (int i = 0; i < coords.length; i += 2) {
            points[i / 2] = new Point2D(coords[i], coords[i + 1]);
        }
        canvas.addToListAndDraw(new Trapeze(points), false);
    }

    public void addPolyline(double[] coords) throws Exception {
        int n = coords.length / 2;
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < coords.length; i += 2) {
            points[i / 2] = new Point2D(coords[i], coords[i + 1]);
        }
        canvas.addToListAndDraw(new Polyline(points), false);
    }

    public void addTGon(double[] coords) throws Exception {
        int n = 3;
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < coords.length; i += 2) {
            points[i / 2] = new Point2D(coords[i], coords[i + 1]);
        }

        canvas.addToListAndDraw(new TGon(points), false);
    }

    public void addSegment(double[] coords) throws Exception {
        canvas.addToListAndDraw(new Segment(new Point2D(coords[0], coords[1]),
                new Point2D(coords[2], coords[3])), false);
    }

    public void addShape(AddFigureData data) throws Exception {
        String type = data.getType();
        double[] coords = new double[data.getCoords().size()];
        for (int i = 0; i < coords.length; i++) {
            coords[i] = data.getCoords().get(i);
        }
        switch (type) {
            case "Circle":
                addCircle(coords);
                break;
            case "Segment":
                addSegment(coords);
                break;
            case "TGon":
                addTGon(coords);
                break;
            case "NGon":
                addNGon(coords);
                break;
            case "QGon":
                addQGon(coords);
                break;
            case "Trapeze":
                addTrapeze(coords);
                break;
            case "Rectangle":
                addRectangle(coords);
                break;
            case "Polyline":
                addPolyline(coords);
                break;
        }
    }

    private void onAddFigure(View view) {
        ((MainActivity) requireActivity()).navigate(AddFigureFragment.createInstance((data) -> {
            try {
                MainFragment.this.addShape(data);
            } catch (Exception e) {
                Log.e(MainFragment.class.toString(), e.getMessage());
            }
        }));
    }

    private void onInitialize(View view) {
        canvas.removeAll(true);
    }

    private void onUploadFromFile(View view) {
        readFileLauncher.launch(new String[]{"text/plain"});
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void onSaveAsImg(View view) {
        Bitmap bitmap = viewToBitmap(canvas);
        ContentValues values = contentValues();
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Geometry");
        values.put(MediaStore.Images.Media.IS_PENDING, true);

        Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
            try {
                saveImageToStream(bitmap, getContext().getContentResolver().openOutputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            values.put(MediaStore.Images.Media.IS_PENDING, false);
            getContext().getContentResolver().update(uri, values, null, null);
        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onDragFigure(View view) {
        List<String> stringList = new ArrayList<>();
        canvas.getList().forEach((shape) -> {
            stringList.add(shape.toString());
        });
        ((MainActivity) requireActivity()).navigate(DragFigureFragment.createInstance((data) -> {
            try {
                MainFragment.this.moveFigure(data);
            } catch (Exception e) {
                Log.e(MainFragment.class.toString(), e.getMessage());
            }
        }, stringList));
    }

    private void onRemoveFigure(View view) {
        List<String> stringList = new ArrayList<>();
        canvas.getList().forEach((shape) -> {
            stringList.add(shape.toString());
        });
        ((MainActivity) requireActivity()).navigate(RemoveFragment.createInstance((data) -> {
            try {
                MainFragment.this.removeFigureByPosition(data);
            } catch (Exception e) {
                Log.e(MainFragment.class.toString(), e.getMessage());
            }
        }, stringList));
    }

    private void onComputeS(View view) {
        List<String> stringList = new ArrayList<>();
        canvas.getList().forEach((shape) -> {
            stringList.add(shape.toString());
        });
        ((MainActivity) requireActivity()).navigate(CountFragment.createInstance((data) -> {
            try {
                MainFragment.this.countByPos(data);
            } catch (Exception e) {
                Log.e(MainFragment.class.toString(), e.getMessage());
            }
        }, stringList, false));
    }

    private void onComputeP(View view) {
        List<String> stringList = new ArrayList<>();
        canvas.getList().forEach((shape) -> {
            stringList.add(shape.toString());
        });
        ((MainActivity) requireActivity()).navigate(CountFragment.createInstance((data) -> {
            try {
                MainFragment.this.countByPos(data);
            } catch (Exception e) {
                Log.e(MainFragment.class.toString(), e.getMessage());
            }
        }, stringList, true));
    }

    private void onCheckIfCross(View view) {
        List<String> stringList = new ArrayList<>();
        canvas.getList().forEach((shape) -> {
            stringList.add(shape.toString());
        });
        ((MainActivity) requireActivity()).navigate(CheckIfCrossFragment.createInstance((data) -> {
            try {
                MainFragment.this.setIfCross(data);
            } catch (Exception e) {
                Log.e(MainFragment.class.toString(), e.getMessage());
            }
        }, stringList));
    }

    private void onAddToDatabase(View view) {
        database.addToDatabase(canvas.getList(), null);
    }

    private void onRetrieveFromDatabase(View view) {
        database.retrieveFromDatabase((restoredList) -> {
            MainFragment.this.requireActivity().runOnUiThread(() -> {
                restoredList.forEach((shape) -> {
                    canvas.addToListAndDraw(shape, true);
                });
            });
        });
    }

    private void writeToFile(View view) {
        writeToFileLauncher.launch(new String[]{"text/plain"});
    }

    @Override
    public void onDestroyView() {
        addFigure = null;
        removeFigure = null;
        saveAsImg = null;
        saveToFile = null;
        computeS = null;
        computeP = null;
        dragFigure = null;
        addToDB = null;
        retrieveFromDB = null;
        clear = null;
        uploadFromFile = null;
        checkIfCross = null;
        super.onDestroyView();
    }
}
