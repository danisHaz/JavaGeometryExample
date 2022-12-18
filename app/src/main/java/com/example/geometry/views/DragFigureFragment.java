package com.example.geometry.views;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geometry.R;
import com.example.geometry.callbacks.DragFigureData;
import com.example.geometry.utils.ICallbackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DragFigureFragment extends Fragment {

    private Button cancel = null;
    private Button drag = null;
    private Spinner type = null;
    private Spinner dragType = null;
    private LinearLayout container = null;
    private final ICallbackHandler<DragFigureData> callback;

    private final List<String> currentFigures;

    //additional fields to add for specific move
    private EditText xField = null;
    private EditText yField = null;
    private Spinner axis = null;

    private DragFigureFragment(ICallbackHandler<DragFigureData> callback,
                               List<String> currentFigures) {
        this.callback = callback;
        this.currentFigures = currentFigures;
    }

    public static DragFigureFragment createInstance(ICallbackHandler<DragFigureData> callback,
                                                    List<String> currentFigures) {
        return new DragFigureFragment(callback, currentFigures);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drag_figure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this::onCancel);

        drag = view.findViewById(R.id.drag);
        drag.setOnClickListener(this::onDragFigure);


        type = view.findViewById(R.id.type);
        dragType = view.findViewById(R.id.dragType);
        container = view.findViewById(R.id.container);
        axis = new Spinner(getContext());
        xField = new EditText(getContext());
        yField = new EditText(getContext());

        setDataToTypes();
        setDataToCombo();
        setAxisSpinner();
        setTextFields();
    }

    private void setTextFields() {
        xField.setSingleLine();
        xField.setInputType(InputType.TYPE_CLASS_NUMBER);
        yField.setSingleLine();
        yField.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void onCancel(View view) {
        getParentFragmentManager().popBackStack();
    }

    private void onDragFigure(View view) {
        String figure = (String) type.getSelectedItem();
        String type = (String) dragType.getSelectedItem();

        if (Objects.equals(figure, "") || Objects.equals(type, "")
                || figure == null || type == null) {
            showAlert("Incorrect data selected");
            return;
        }

        for (int i = 0; i < currentFigures.size(); i++) {
            if (currentFigures.get(i).equals(figure)) {
                List<Double> args = new ArrayList<>();
                if (type.equals("SymAxis")) {
                    args.add(Double.parseDouble((String) axis.getSelectedItem()));
                } else if (type.equals("Rot")) {
                    args.add(Double.parseDouble(xField.getText().toString()));
                } else {
                    args.add(Double.parseDouble(xField.getText().toString()));
                    args.add(Double.parseDouble(yField.getText().toString()));
                }
                callback.handleEvent(new DragFigureData(i, type, args));
                onCancel(view);
                return;
            }
        }

        showAlert("Invalid data state");
    }

    private void changeLayoutOnSelectedDrag(String dragType) {
        clearLayout();

        if (Objects.equals(dragType, "Rot")) {
            addFields(0);
        } else if (Objects.equals(dragType, "SymAxis")) {
            addAxisSpinner();
        } else {
            addFields(1);
        }
        container.requestLayout();
    }

    private void setDataToTypes() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                R.layout.small_spinner_item,
                currentFigures);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
    }

    private void setDataToCombo() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.drag_types,
                android.R.layout.simple_list_item_1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dragType.setAdapter(adapter);

        dragType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] choose = getResources().getStringArray(R.array.drag_types);
                DragFigureFragment.this.changeLayoutOnSelectedDrag(choose[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // pass
            }
        });
    }

    private void setAxisSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.axis_numbers,
                android.R.layout.simple_list_item_1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        axis.setAdapter(adapter);
    }

    private void clearLayout() {
        container.removeAllViews();
    }

    private void addFields(int combination) {
        container.addView(xField);
        if (combination == 1) {
            container.addView(yField);
        }
    }

    private void addAxisSpinner() {
        if (axis != null) {
            container.addView(axis);
        }
    }

    private void showAlert(CharSequence text) {
        Toast.makeText(getContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }
}
