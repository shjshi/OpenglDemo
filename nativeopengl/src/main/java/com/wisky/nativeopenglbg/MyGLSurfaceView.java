package com.wisky.nativeopenglbg;

import android.content.Context;
import android.opengl.GLSurfaceView;

class MyGLSurfaceView extends GLSurfaceView {

    private final GLES3Render renderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new GLES3Render();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}
