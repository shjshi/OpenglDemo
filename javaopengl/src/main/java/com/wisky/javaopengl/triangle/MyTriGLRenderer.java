package com.wisky.javaopengl.triangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;


public class MyTriGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }



    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    // 一直被回调？对应c中的哪个方法？
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e("shj","onDrawFrame");
        mTriangle.draw();
    }
}

