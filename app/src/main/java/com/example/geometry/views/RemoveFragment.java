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
import com.example.geometry.callbacks.RemoveData;
import com.example.geometry.utils.ICallbackHandler;

import java.util.List;

import app.IShape;

public class RemoveFragment extends Fragment {

    private final ICallbackHandler<RemoveData> callback;
    private final List<String> currentFigures;

    private Button cancel = null;
    private Button remove = null;
    private Spinner type = null;

    private RemoveFragment(ICallbackHandler<RemoveData> callback,
                          List<String> currentFigures) {
        this.callback = callback;
        this.currentFigures = currentFigures;
    }

    public static RemoveFragment createInstance(ICallbackHandler<RemoveData> callback,
                                               List<String> currentFigures) {
        return new RemoveFragment(callback, currentFigures);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remove, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this::onCancel);

        remove = view.findViewById(R.id.remove);
        remove.setOnClickListener(this::onRemove);


        type = view.findViewById(R.id.type);

        setDataToTypes();
    }

    private void onCancel(View view) {
        getParentFragmentManager().popBackStack();
    }

    private void onRemove(View view) {
        String obj = (String) type.getSelectedItem();
        for (int i = 0; i < currentFigures.size(); i++) {
            if (currentFigures.get(i).equals(obj)) {
                callback.handleEvent(new RemoveData(i));
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
