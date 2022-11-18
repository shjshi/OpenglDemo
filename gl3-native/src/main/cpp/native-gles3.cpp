#include <jni.h>
#include <string>

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

#include <android/log.h>
#include "LogUtils.h"

/**
 * 顶点着色器源码
 * auto 关键字啥意思？
 * #version 300 es 声明着色器的版本
 * vPosition是一个变量名字
 * 作用是什么？
 */
auto gl_vertexShader_source =
        "#version 300 es\n"
        "layout(location = 0) in vec4 vPosition;\n"
        "void main() {\n"
        "   gl_Position = vPosition;\n"
        "}\n";

/**
 * 片段着色器源码
 * 作用是什么？
 * 三角形的背景色？
 */
auto gl_fragmentShader_source =
        "#version 300 es\n"
        "precision mediump float;\n"
        "out vec4 fragColor;\n"
        "void main() {\n"
        "   fragColor = vec4(1.0,1.0,0.0,1.0);\n"
        "}\n";

/**
 * 输出GL的属性值
 */
static void printGLString(const char *name, GLenum s) {
    const char *glName = reinterpret_cast<const char *>(glGetString(s));
    LOGE("GL %s = %s", name, glName);
}

static void checkGlError(const char *op) {
    for (GLint error = glGetError(); error; error = glGetError()) {
        LOGI("after %s() glError (0x%x)\n", op, error);
    }
}

/**
 * 编译着色器源码
 *
 * 编译着色器，不是一种着色器，是把着色器进行编译
 *
 * @param shaderType 着色器类型
 * @param shaderSource  源码
 * @return
 */
GLuint compileShader(GLenum shaderType, const char *shaderSource) {
    //创建着色器对象
    GLuint shader = glCreateShader(shaderType);
    if (!shader) {
        return 0;
    }
    //加载着色器源程序
    glShaderSource(shader, 1, &shaderSource, nullptr);
    //编译着色器程序
    glCompileShader(shader);

    //获取编译状态
    GLint compileRes;

    // 根据第二个参数，把方法执行的数据赋值第三个参数
    // &compileRes表示compileRes的地址？
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compileRes);

    if (!compileRes) {
        //获取日志长度
        GLint infoLen = 0;

        glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);

        if (infoLen > 0) {
            // molloc 根据日志大小开辟空间
            // 指针
            // static_cast:将void*转换为char *?
            char *infoLog = static_cast<char *>(malloc(sizeof(char) * infoLen));
            //获取日志信息
            glGetShaderInfoLog(shader, infoLen, nullptr, infoLog);
            LOGE("compile shader error : %s", infoLog);

            // malloc.h的方法,释放日志所占用空间
            free(infoLog);
        }
        //删除着色器
        glDeleteShader(shader);
        return 0;
    }
    return shader;
}


/**
 * 链接着色器程序
 *
 * 把着色器 链接
 */
GLuint linkProgram(GLuint vertexShader, GLuint fragmentShader) {
    //创建程序
    GLuint programObj = glCreateProgram();
    if (programObj == 0) {
        LOGE("create program error");
        return 0;
    }
    //加载着色器载入程序
    glAttachShader(programObj, vertexShader);
    checkGlError("glAttachShader");

    glAttachShader(programObj, fragmentShader);
    checkGlError("glAttachShader");

    //链接着色器程序
    glLinkProgram(programObj);

    //检查程序链接状态
    GLint linkRes;
    glGetProgramiv(programObj, GL_LINK_STATUS, &linkRes);

    if (!linkRes) {//链接失败
        //获取日志长度
        GLint infoLen;
        glGetProgramiv(programObj, GL_INFO_LOG_LENGTH, &infoLen);
        if (infoLen > 1) {
            //获取并输出日志
            char *infoLog = static_cast<char *>(malloc(sizeof(char) * infoLen));
            glGetProgramInfoLog(programObj, infoLen, nullptr, infoLog);
            LOGE("Error link program : %s", infoLog);
            free(infoLog);
        }
        //删除着色器程序
        glDeleteProgram(programObj);
        return 0;
    }
    return programObj;
}


/**
 * 着色器程序
 */
GLuint program;

extern "C"
JNIEXPORT void JNICALL
Java_com_lxk_hellogles3_GLES3Render_surfaceChanged(JNIEnv *env, jobject thiz, jint w, jint h) {
    printGLString("Version", GL_VERSION);
    printGLString("Vendor", GL_VENDOR);
    printGLString("Renderer", GL_RENDERER);
    printGLString("Extension", GL_EXTENSIONS);

    LOGD("surfaceChange(%d,%d)", w, h);

    //编译着色器源码
    GLuint vertexShader = compileShader(GL_VERTEX_SHADER, gl_vertexShader_source);
    GLuint fragmentShader = compileShader(GL_FRAGMENT_SHADER, gl_fragmentShader_source);
    //链接着色器程序
    program = linkProgram(vertexShader, fragmentShader);

    if (!program) {
        LOGE("linkProgram error");
        return;
    }

    //设置程序窗口
    glViewport(0, 0, w, h);
    checkGlError("glViewport");
}

/**
 * 顶点坐标
 * 是一个3d坐标
 *
 * 是一个c数组？
 * 里面的数值是什么意思?
 *
 * x,y,z
 * z = 0.0f
 */
const GLfloat vVertex[] = {
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
};

// float类型，
static float r;
static float g;
static float b;

/**
 * 修改背景颜色
 *
 * r,g,b
 */
void changeBg() {
    r += 0.01f;
    if (r > 1.0f) {
        g += 0.01f;
        if (g > 1.0f) {
            b += 0.01f;
            if (b > 1.0f) {
                r = 0.01f;
                g = 0.01f;
                b = 0.01f;
            }
        }
    }
}

/**
 * 顶点属性索引
 */
GLuint vertexIndex = 0;

extern "C"
JNIEXPORT void JNICALL
Java_com_lxk_hellogles3_GLES3Render_drawFrame(JNIEnv *env, jobject thiz) {

    //改变颜色值
//    changeBg();

    glClearColor(1, 1,1, 1.0f);
    checkGlError("glClearColor");
    //清空颜色缓冲区
    glClear(GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");

    //设置为活动程序
    glUseProgram(program);
    checkGlError("glUseProgram");


    //加载顶点坐标
    glVertexAttribPointer(vertexIndex, 3, GL_FLOAT, GL_FALSE, 0, vVertex);
    checkGlError("glVertexAttribPointer");

    //启用通用顶点属性数组
    glEnableVertexAttribArray(vertexIndex);
    checkGlError("glEnableVertexAttribArray");

    //绘制三角形
    glDrawArrays(GL_TRIANGLES, 0, 3);
    checkGlError("glDrawArrays");

    //禁用通用顶点属性数组
    glDisableVertexAttribArray(vertexIndex);
    checkGlError("glDisableVertexAttribArray");
}