package com.example.geometry.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geometry.R;
import com.example.geometry.callbacks.CheckIfCrossData;
import com.example.geometry.utils.ICallbackHandler;

import java.util.List;
import java.util.stream.Collectors;

public class CheckIfCrossFragment extends Fragment {

    private final ICallbackHandler<CheckIfCrossData> callback;
    private final List<String> currentFigures;

    private Spinner type = null;
    private Spinner firstShape = null;
    private Spinner secondShape = null;
    private Button checkIfCross = null;
    private Button cancel = null;

    private CheckIfCrossFragment(ICallbackHandler<CheckIfCrossData> callback,
                                 List<String> currentFigures) {
        this.callback = callback;
        this.currentFigures = currentFigures;
    }

    public static CheckIfCrossFragment createInstance(ICallbackHandler<CheckIfCrossData> callback,
                                                      List<String> currentFigures) {
        return new CheckIfCrossFragment(callback, currentFigures);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_cross, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this::onCancel);

        checkIfCross = view.findViewById(R.id.checkIfCross);
        checkIfCross.setOnClickListener(this::onCheckIfCross);

        firstShape = view.findViewById(R.id.firstShape);
        secondShape = view.findViewById(R.id.secondShape);
        type = view.findViewById(R.id.type);

        setDataToCombo();
    }

    private void onCheckIfCross(View view) {
        String firstOne = (String) firstShape.getSelectedItem();
        String secondOne = (String) secondShape.getSelectedItem();

        if (firstOne == null || secondOne == null
                || firstOne.equals("") || secondOne.equals("")) {
            showAlert("Data invalid");
            return;
        }

        int firstPosition = -1;
        int secondPosition = 0;
        for (int i = 0; i < currentFigures.size(); i++) {
            if (currentFigures.get(i).equals(firstOne)) {
                firstPosition = i;
            }

            if (currentFigures.get(i).equals(secondOne)) {
                secondPosition = i;
            }
        }

        callback.handleEvent(new CheckIfCrossData(firstPosition, secondPosition));
        onCancel(view);
    }

    private void onCancel(View view) {
        getParentFragmentManager().popBackStack();
    }

    private void setDataToTypes(String shapeTypeFilter) {
        List<String> filtered = currentFigures.stream()
                .filter((shapeConstr) -> shapeConstr.split("\\(")[0].equals(shapeTypeFilter))
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                R.layout.small_spinner_item,
                filtered);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstShape.setAdapter(adapter);
        secondShape.setAdapter(adapter);
    }

    private void setDataToCombo() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.shape_types,
                android.R.layout.simple_list_item_1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] choose = getResources().getStringArray(R.array.shape_types);
                CheckIfCrossFragment.this.setDataToTypes(choose[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // pass
            }
        });
    }

    private void showAlert(CharSequence text) {
        Toast.makeText(getContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }
}
