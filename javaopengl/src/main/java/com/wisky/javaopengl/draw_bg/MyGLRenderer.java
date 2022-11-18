package com.wisky.javaopengl.draw_bg;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    // 每次重新绘制view时调用
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    // 调用一次以设置view's OpenGL ES environment.
    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    // 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用。
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
