package com.wisky.javaopengl.triangle;

import static java.lang.Math.sin;

import static javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY;

import android.opengl.EGL14;
import android.opengl.EGLDisplay;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLSurface;

public class Triangle {

    // number of coordinates per vertex in this array
    // 此数组中每个顶点的坐标数,3维坐标，3个表示一个点
    static final int COORDS_PER_VERTEX = 3;


    // 3维数组 顶点坐标
    static float[] triangleCoords = {   // in counterclockwise order:逆时针顺序
            0.0f, 1f, 0.0f, // top
            -1f, 0f, 0.0f, // bottom left
            1f, 0f, 0.0f  // bottom right
    };


    private final int mProgram;

    // 9/3 = 3
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;



    // 顶点坐标
    private FloatBuffer vertexBuffer;

    public Triangle() {
        initVertexBuffer();
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();
        initProgram();
    }

    private void initProgram() {
        // vPosition对应着顶点坐标vertexBuffer
        final String vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = vPosition;" +
                        "}";
        final String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";
        int vertexShader = MyTriGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyTriGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    private void initVertexBuffer() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // 一个float4个字节
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate 设置缓冲区读取第一个坐标
        vertexBuffer.position(0);
    }

    public void draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        glVertexAttribPointer_vertexBuffer(positionHandle);


        glUniform4fv_color();

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);


        // swap buffers and poll IO events
//        glfwSwapBuffers(window);
//        glfwPollEvents();
        // EGLDisplay display, EGLSurface surface
        // GLSurfaceView
//        EGLDisplay eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
//        EGLSurface surface = EGL14.
//        EGL10.eglSwapBuffers(eglDisplay,)
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private void glVertexAttribPointer_vertexBuffer(int positionHandle) {
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // 3 * 4 = 12
        // 4 bytes per vertex
        int vertexStride = COORDS_PER_VERTEX * 4;

        /**
         *         处理顶点数据，把顶点坐标传到顶点着色器？vPosition对应着顶点坐标
         *         handle
         *         三维坐标，所以传递3
         *         数据类型float
         *         normalized
         *         stride
         *         顶点坐标数据
         */
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
    }

    private void glUniform4fv_color() {

        // glGetUniformLocation查询uniform vColor的位置值
        // get handle to fragment shader's vColor member
        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        float timeValue = System.currentTimeMillis();

        // 使用sin函数让颜色在0.0到1.0之间改变
        float greenValue = (float) ((sin(timeValue) / 2.0f) + 0.5f);
        // Set color with red, green, blue and alpha (opacity) values
        float[] triColor = {0f, 1f, 0f, 1.0f};
        Log.e("shj","greenValue "+greenValue);

        /**
         * 是否跟片段着色器有关系呢？
         * handle
         * count？为啥是1？count就是定义的uniform变量的数量，如果定义的unifom变量是数组就是1+，如果不是数组，就是1
         * 颜色数据
         * offset
         */
//        GLES20.glUniform4fv(colorHandle,1,triColor,0);
        // Set color for drawing the triangle

        GLES20.glUniform4f(colorHandle, 1, greenValue, 1,1f);
    }
}

