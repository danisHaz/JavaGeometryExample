package com.example.geometry.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;

import com.example.geometry.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMainFragment();
    }

    public void navigate(Class<? extends Fragment> to) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, to, null)
                .setReorderingAllowed(true)
                .addToBackStack(to.getName())
                .commit();
    }

    public void navigate(Fragment to) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, to, to.getClass().toString())
                .setReorderingAllowed(true)
                .addToBackStack(to.getClass().toString())
                .commit();
    }

    private void setMainFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, MainFragment.class, null)
                .setReorderingAllowed(true)
                .commit();
    }
}