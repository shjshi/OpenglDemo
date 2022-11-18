package com.wisky.nativeopenglbg;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * 画一个背景色
 */
public class OpenGLES20Activity extends Activity {

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
