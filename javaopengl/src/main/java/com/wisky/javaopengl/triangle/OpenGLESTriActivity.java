package com.wisky.javaopengl.triangle;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * 画一个背景色
 */
public class OpenGLESTriActivity extends Activity {

    private GLSurfaceView gLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }
}
