package com.wisky.nativeopenglbg;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class GLES3Render implements GLSurfaceView.Renderer {
    static {
        System.loadLibrary("native-gles3");
    }

    public native void surfaceChanged(int w, int h);

    public native void drawFrame();

    public native void surfaceCreated();
    // 每次重新绘制view时调用
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        drawFrame();
    }

    // 调用一次以设置view's OpenGL ES environment.
    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        surfaceCreated();
    }

    // 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用。
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        surfaceChanged(width,height);
    }
}
