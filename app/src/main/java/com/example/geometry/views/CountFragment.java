package com.example.geometry.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geometry.R;
import com.example.geometry.callbacks.CountData;
import com.example.geometry.utils.ICallbackHandler;

import java.util.List;

public class CountFragment extends Fragment {

    private final ICallbackHandler<CountData> callback;
    private final List<String> currentFigures;
    private final boolean countType;

    private Button cancel = null;
    private Button count = null;
    private Spinner type = null;

    private CountFragment(ICallbackHandler<CountData> callback,
                          List<String> currentFigures, boolean type) {
        this.callback = callback;
        this.currentFigures = currentFigures;
        this.countType = type;
    }

    public static CountFragment createInstance(ICallbackHandler<CountData> callback,
                                               List<String> currentFigures, boolean type) {
        return new CountFragment(callback, currentFigures, type);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_count, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this::onCancel);

        count = view.findViewById(R.id.count);
        count.setOnClickListener(this::onCount);


        type = view.findViewById(R.id.type);

        setDataToTypes();
    }

    private void onCancel(View view) {
        getParentFragmentManager().popBackStack();
    }

    private void onCount(View view) {
        for (int i = 0; i < currentFigures.size(); i++) {
            if (currentFigures.get(i).equals(type.getSelectedItem())) {
                callback.handleEvent(new CountData(i, countType));
                onCancel(view);
                return;
            }
        }
    }

    private void setDataToTypes() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                R.layout.small_spinner_item,
                currentFigures);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
    }
}
