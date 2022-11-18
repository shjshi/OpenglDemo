package com.wisky.javaopengl.changegray;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    // Additional member variables
    private int mWidth;
    private int mHeight;
//    private final float[] POINT_DATA = {
//            -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f
//    };

    private final float[] POINT_DATA = {
            -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f
    };
    private final float[] TEX_VERTEX = {0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f};
    // Handle to a program object
    private int mProgramObject;
    private int aPositionLoc;
    private int aTextureLoc;
    private int aTextureSampler;
    private FloatBuffer mVertices;
    private FloatBuffer mTextures;
    // Texture handle
    private int mTextureId;

    // 每次重新绘制view时调用
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the viewport
        GLES20.glViewport(0, 0, mWidth, mHeight);

        // Clear the color buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Bind the texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);

        // Set the sampler texture unit to 0
        GLES20.glUniform1i(aTextureSampler, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT);
        
    }

    // 调用一次以设置view's OpenGL ES environment.
    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        String vShaderStr =
                "attribute vec4 a_Position;   \n" +
                        "attribute vec2 a_TexCoord;   \n" +
                        "varying vec2 v_TexCoord;     	  				\n" +
                        "void main()                  				\n" +
                        "{                            				\n" +
                        "   gl_Position = a_Position; 				\n" +
                        "   v_TexCoord = a_TexCoord;  				\n" +
                        "}                            				\n";

        String fShaderStr =
                "precision mediump float;                           \n" +
                        "varying vec2 v_TexCoord;                            	 \n" +
                        "uniform sampler2D u_TextureUnit;                     \n" +
                        "void main()                                         \n" +
                        "{                                                   \n" +
                        "vec4 src = texture2D(u_TextureUnit, v_TexCoord);         \n" +
                        "float gray = (src.r + src.g + src.b) / 3.0;       \n" +
                        "  gl_FragColor = vec4(gray, gray, gray, 1.0);     \n" +
                        "}                                                   \n";

//        String vShaderStr =
//                        "attribute vec4 a_Position;   \n" +
//                        "attribute vec2 a_TexCoord;   \n" +
//                        "varying vec2 v_TexCoord;      	  				\n" +
//                        "void main()                  				\n" +
//                        "{                            				\n" +
//                        "   gl_Position = a_Position; 				\n" +
//                        "   v_TexCoord = a_TexCoord;  				\n" +
//                        "}                            				\n";
//
//        String fShaderStr =
//                        "precision mediump float;                            \n" +
//                        "varying vec2 v_TexCoord;                             	 \n" +
//                        "uniform sampler2D u_TextureUnit;                       \n" +
//                        "void main()                                         \n" +
//                        "{                                                   \n" +
//                        "  gl_FragColor = texture2D( u_TextureUnit, v_TexCoord );      \n" +
//                        "} \n";
        // Load the shaders and get a linked program object
        mProgramObject = ShaderUtils.createProgram(vShaderStr, fShaderStr);
        GLES20.glUseProgram(mProgramObject);
        // Get the location
        aPositionLoc = GLES20.glGetAttribLocation(mProgramObject, "a_Position");
        aTextureLoc = GLES20.glGetAttribLocation(mProgramObject, "a_TexCoord");
        aTextureSampler = GLES20.glGetUniformLocation(mProgramObject, "u_TextureUnit");


        mVertices = ByteBuffer.allocateDirect(POINT_DATA.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextures = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(POINT_DATA).position(0);
        mTextures.put(TEX_VERTEX).position(0);
        GLES20.glVertexAttribPointer(aPositionLoc,2, GLES20.GL_FLOAT,false,0,mVertices);
        GLES20.glVertexAttribPointer(aTextureLoc,2, GLES20.GL_FLOAT,false,0,mTextures);
        GLES20.glEnableVertexAttribArray(aPositionLoc);
        GLES20.glEnableVertexAttribArray(aTextureLoc);
        
        //纹理
        mTextureId = createSimpleTexture2D();

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    }

    private int createSimpleTexture2D() {
        // Texture object handle
        int[] textureId = new int[1];

        // 3x3 Image, 3 bytes per pixel (R, G, B)
        byte[] pixels =
                {
                        0, (byte) 0xff, 0, // Green
                        0, 0, (byte) 0xff, // Blue
                        (byte) 0xff, 0, 0, // Red
                        (byte) 0xff, 0, 0, // Red
                        0, (byte) 0xff, 0, // Green
                        (byte) 0xff, (byte) 0xff, 0,// Yellow
                        0, 0, (byte) 0xff, // Blue
                        (byte) 0xff, (byte) 0xff, 0,// Yellow
                        (byte) 0xff, 0, 0 // Red

                };

        ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(9* 3);
        pixelBuffer.put(pixels).position(0);

        // Use tightly packed data
        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);

        //  Generate a texture object
        GLES20.glGenTextures(1, textureId, 0);

        // Bind the texture object
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        //  Load the texture
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 3, 3, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);

        // Set the filtering mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        return textureId[0];
    }

    // 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用。
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mWidth = width;
        mHeight = height;
    }
}
