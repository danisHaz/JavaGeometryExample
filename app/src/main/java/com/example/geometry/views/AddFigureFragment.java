package com.example.geometry.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geometry.R;
import com.example.geometry.callbacks.AddFigureData;
import com.example.geometry.utils.ICallbackHandler;

import java.util.ArrayList;
import java.util.List;

public class AddFigureFragment extends Fragment {

    private Spinner spinner = null;
    private Button addFigure = null;
    private Button cancel = null;
    private Spinner pointNumber = null;
    private LinearLayout xLayout = null;
    private LinearLayout yLayout = null;
    private String selectedFigure = null;
    private ICallbackHandler<AddFigureData> callback = null;

    private AddFigureFragment(ICallbackHandler<AddFigureData> callback) {
        this.callback = callback;
    }

    public static AddFigureFragment createInstance(ICallbackHandler<AddFigureData> callback) {
        return new AddFigureFragment(callback);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_figure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.types);

        addFigure = view.findViewById(R.id.add);
        addFigure.setOnClickListener(this::onAddFigure);

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this::onCancel);

        xLayout = view.findViewById(R.id.xLayout);
        yLayout = view.findViewById(R.id.yLayout);

        pointNumber = view.findViewById(R.id.pointNumber);

        setPointNumber();
        setDataToCombo();
    }

    @Override
    public void onDestroy() {
        spinner = null;
        addFigure = null;
        cancel = null;
        xLayout = null;
        yLayout = null;
        pointNumber = null;
        super.onDestroy();
    }

    public void setDataToCombo() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.shape_types,
                android.R.layout.simple_list_item_1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] choose = getResources().getStringArray(R.array.shape_types);
                AddFigureFragment.this.addAttrs(choose[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // pass
            }
        });
    }

    public void addAttrs(String type) {
        selectedFigure = type;
        switch (type) {
            case "Circle":
                addCircle();
                break;
            case "Segment":
                addSegment();
                break;
            case "TGon":
                addTGon();
                break;
            case "NGon":
                addNGon();
                break;
            case "QGon":
                addQGon();
                break;
            case "Trapeze":
                addTrapeze();
                break;
            case "Rectangle":
                addRectangle();
                break;
            case "Polyline":
                addPolyline();
                break;
        }
    }

    private void onAddFigure(View view) {
        List<Double> arr = new ArrayList<>();
        for (int pos = 0; pos < xLayout.getChildCount(); pos++) {
            if (xLayout.getChildAt(pos).getVisibility() == View.INVISIBLE) {
                break;
            }
            arr.add(Double.parseDouble(((EditText)xLayout.getChildAt(pos)).getText().toString()));
            if (yLayout.getChildAt(pos).getVisibility() == View.VISIBLE) {
                arr.add(Double.parseDouble(((EditText)yLayout.getChildAt(pos)).getText().toString()));
            }
        }

        callback.handleEvent(new AddFigureData(selectedFigure, arr));
        onCancel(view);
    }

    private void onCancel(View view) {
        getParentFragmentManager().popBackStack();
    }

    private void removePointNumber() {
        if (pointNumber != null) {
            pointNumber.setVisibility(View.INVISIBLE);
        }
    }

    private void addPointNumber() {
        if (pointNumber != null) {
            pointNumber.setVisibility(View.VISIBLE);
        }
    }

    private void setPointNumber() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.number_of_vertices,
                android.R.layout.simple_list_item_1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointNumber.setAdapter(adapter);

        pointNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] choose = getResources().getStringArray(R.array.number_of_vertices);
                clearLayout();
                addNFields(Integer.parseInt(choose[i]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // pass
            }
        });
    }

    private void clearLayout() {
        for (int pos = 0; pos < xLayout.getChildCount(); pos++) {
            View child = xLayout.getChildAt(pos);
            child.setVisibility(View.INVISIBLE);

            child = yLayout.getChildAt(pos);
            child.setVisibility(View.INVISIBLE);
        }
    }

    private void changeFieldVisibility(int position) {
        View child = xLayout.getChildAt(position);
        if (child.getVisibility() == View.VISIBLE) {
            child.setVisibility(View.INVISIBLE);
        } else {
            child.setVisibility(View.VISIBLE);
        }
    }

    private void addNFields(int n) {
        for (int pos = 0; pos < Math.min(n, xLayout.getChildCount()); pos++) {
            View xChild = xLayout.getChildAt(pos);
            View yChild = yLayout.getChildAt(pos);
            xChild.setVisibility(View.VISIBLE);
            yChild.setVisibility(View.VISIBLE);
        }
    }

    private void addCircle() {
        removePointNumber();
        clearLayout();
        addNFields(1);
        changeFieldVisibility(1);
    }

    private void addSegment() {
        removePointNumber();
        clearLayout();
        addNFields(2);
    }

    private void addTGon() {
        removePointNumber();
        clearLayout();
        addNFields(3);
    }

    private void addNGon() {
        removePointNumber();
        clearLayout();
        addPointNumber();
        String[] choose = getResources().getStringArray(R.array.number_of_vertices);
        addNFields(Integer.parseInt(choose[pointNumber.getSelectedItemPosition()]));
    }

    private void addQGon() {
        removePointNumber();
        clearLayout();
        addNFields(4);
    }

    private void addTrapeze() {
        removePointNumber();
        addQGon();
    }

    private void addRectangle() {
        removePointNumber();
        addQGon();
    }

    private void addPolyline() {
        addNGon();
    }

}
