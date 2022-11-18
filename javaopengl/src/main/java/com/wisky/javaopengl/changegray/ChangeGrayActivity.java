package com.wisky.javaopengl.changegray;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * 参考链接 https://developer.android.com/training/graphics/opengl/environment?hl=zh-cn
 * 显示黑屏的简单 Android 应用
 */
public class ChangeGrayActivity extends Activity {

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
