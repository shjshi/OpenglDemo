package com.wisky.javaopengl.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;

class MyGLSurfaceView extends GLSurfaceView {

    private final MyTriGLRenderer  renderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new MyTriGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}
