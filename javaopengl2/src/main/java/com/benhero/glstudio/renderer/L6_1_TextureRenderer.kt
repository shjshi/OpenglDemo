package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.util.Log
import com.benhero.glstudio.R
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import com.benhero.glstudio.util.DisplayUtil
import com.benhero.glstudio.util.ProjectionMatrixHelper
import com.benhero.glstudio.util.TextureHelper
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 纹理绘制
 *
 * @author Benhero
 */
class L6_1_TextureRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        private val VERTEX_SHADER = """
                uniform mat4 u_Matrix;
                attribute vec4 a_Position;
                // 纹理坐标：2个分量，S和T坐标
                attribute vec2 a_TexCoord;
                varying vec2 v_TexCoord;
                void main() {
                    v_TexCoord = a_TexCoord;
                    //gl_Position = u_Matrix * a_Position;
                    gl_Position = a_Position;
                }
                """
        //
        private val FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                // sampler2D：二维纹理数据的数组 纹理单元
                uniform sampler2D u_TextureUnit;
                void main() {
                    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);
                }
                """

        // 顶点坐标中每个点占的向量个数
        private val POSITION_COMPONENT_COUNT = 2

        //        private val POINT_DATA = floatArrayOf(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f)
        // 顶点坐标
        private val POINT_DATA = floatArrayOf(
            -1f, -1f,
            -1f, 1f,
            1f, 1f,
            1f, -1f
        )

        /**
         * 纹理坐标
         */
        private val TEX_VERTEX = floatArrayOf(
            0f, 1f,
            0f, 0f,
            1f, 0f,
            1f, 1f
        )

        /**
         * 纹理坐标中每个点占的向量个数
         */
        private val TEX_VERTEX_COMPONENT_COUNT = 2
    }

    private val mVertexData: FloatBuffer

    private var uTextureUnitLocation: Int = 0
    private val mTexVertexBuffer: FloatBuffer

    /**
     * 纹理数据
     */
    private var mTextureBean: TextureHelper.TextureBean? = null

    // 正交投影
    private var mProjectionMatrixHelper: ProjectionMatrixHelper? = null

    init {
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA)
        mTexVertexBuffer = BufferUtil.createFloatBuffer(TEX_VERTEX)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {

        // program
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)

        // a_Position在顶点着色器中定义
        val aPositionLocation = getAttrib("a_Position")
        mProjectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")

        // 纹理坐标索引
        val aTexCoordLocation = getAttrib("a_TexCoord")
        uTextureUnitLocation = getUniform("u_TextureUnit")

        // 纹理数据
        mTextureBean = TextureHelper.loadTexture(context, R.drawable.pikachu)


        val width = DisplayUtil.getWindowWidth(context)
        val height = DisplayUtil.getWindowHeight(context)
        Log.e("shj", "$width $height")
        Log.e(
            "shj",
            "mTextureBean!!.textureId ${mTextureBean!!.textureId} ${mTextureBean!!.width} ${mTextureBean!!.height}"
        )

        mVertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT, false, 0, mVertexData
        )
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        // 加载纹理坐标
        mTexVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(
            aTexCoordLocation,
            TEX_VERTEX_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            0,
            mTexVertexBuffer
        )
        GLES20.glEnableVertexAttribArray(aTexCoordLocation)

        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(GL_COLOR_BUFFER_BIT)

        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND)
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        Log.e("shj", "onSurfaceChanged $width $height")
        GLES20.glViewport(0, 0, width, height)
        mProjectionMatrixHelper!!.enable(width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT)
        // 纹理单元：在OpenGL中，纹理 不是 直接绘制到 片段着色器上，而是通过 纹理单元 去保存纹理

        // 设置当前活动的 纹理单元 为 纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

        // 将 纹理ID 绑定到 当前活动的 纹理单元 上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean!!.textureId)

        //
        // 将 纹理单元 传递 片段着色器 的 u_TextureUnit
        // 第二个参数0是什么意思？是纹理单元id
        GLES20.glUniform1i(uTextureUnitLocation, 0)

        // POINT_DATA应该是顶点坐标?
        // POINT_DATA.size / POSITION_COMPONENT_COUNT 除以2是因为两个数构成一个点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.size / POSITION_COMPONENT_COUNT)
    }
}
