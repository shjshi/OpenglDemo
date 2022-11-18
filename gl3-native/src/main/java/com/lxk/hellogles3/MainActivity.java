package com.lxk.hellogles3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * https://github.com/103style
 * https://blog.csdn.net/lxk_1993/article/details/100124007
 * 画一个三角形
 */
public class MainActivity extends AppCompatActivity {

    private GLES3JNIView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GLES3JNIView(getApplication());
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
