package com.wisky.hellotriangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class HelloTriSurfaceView extends GLSurfaceView {
    public HelloTriSurfaceView(Context context) {
        this(context,null);
    }

    public HelloTriSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置 OpenGL ES 的版本
        setEGLContextClientVersion(3);
        setRenderer(new HelloTriRender());
    }
}
