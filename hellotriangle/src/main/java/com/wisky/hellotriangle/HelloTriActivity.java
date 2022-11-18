package com.wisky.hellotriangle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HelloTriActivity extends AppCompatActivity {

    private HelloTriSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new HelloTriSurfaceView(getApplication());
        setContentView(view);
    }


    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }
}