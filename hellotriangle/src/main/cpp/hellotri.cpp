//
// Created by wisky on 2022/8/9.
//
#include <jni.h>
#include <string>
#include <GLES3/gl3.h>
#include <cstdlib>
#include <android/log.h>
#include "LogUtils.h"

/**
 * 顶点着色器源码
 */
auto gl_vertexShader_source =
        "#version 300 es\n"
        "layout(location = 0) in vec4 vPosition;\n"
        "out vec4 vertexColor; // 为片段着色器指定一个颜色输出\n"
        "void main() {\n"
        "   gl_Position = vPosition;\n"
        "   vertexColor = vec4(0.5, 1.0, 0.0, 1.0); // 把输出变量设置为暗红色\n"
        "}\n";

/**
 * 片段着色器源码
 */
auto gl_fragmentShader_source =
        "#version 300 es\n"
        "precision mediump float;\n"
        "out vec4 fragColor;\n"
        "in vec4 vertexColor;\n"
        "void main() {\n"
        "   fragColor = vertexColor;\n"
        "}\n";


/////////////三//////////////
/**
 * 顶点着色器源码
 */
auto gl_vertexShader_source3 =
        "#version 300 es\n"
        "layout(location = 0) in vec4 vPosition;\n"
        "void main() {\n"
        "   gl_Position = vPosition;\n"
        "}\n";

/**
 * 片段着色器源码
 */
auto gl_fragmentShader_source3 =
        "#version 300 es\n"
        "precision mediump float;\n"
        "out vec4 fragColor;\n"
        "uniform vec4 vertexColor;\n"
        "void main() {\n"
        "   fragColor = vertexColor;\n"
        "}\n";
///////////////////////////

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

float vertices[] = {
        -1.0f, -1.0f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.0f,  0.5f, 0.0f
};
/**
 * 着色器程序
 */
GLuint program;

void Init() {

    // 1 编译着色器源码
//    GLuint vertexShader = compileShader(GL_VERTEX_SHADER, gl_vertexShader_source);
//    GLuint fragmentShader = compileShader(GL_FRAGMENT_SHADER, gl_fragmentShader_source);
    GLuint vertexShader = compileShader(GL_VERTEX_SHADER, gl_vertexShader_source3);
    GLuint fragmentShader = compileShader(GL_FRAGMENT_SHADER, gl_fragmentShader_source3);
    //链接着色器程序
    program = linkProgram(vertexShader, fragmentShader);

    // 2 VBO
    unsigned int VBO;
    glGenBuffers(1, &VBO);
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);


    // 3 . 设置顶点属性指针
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    // 4
    glClearColor(1, 1,1, 1.0f);
    checkGlError("glClearColor");
    //清空颜色缓冲区
    glClear(GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");

    // 5 . 当我们渲染一个物体时要使用着色器程序
    glUseProgram(program);

    GLint colorLocation = glGetUniformLocation(program, "vertexColor");
    glUniform4f(colorLocation,1,1,0,1);

    // 6 . 绘制物体
    glDrawArrays(GL_TRIANGLES, 0, 3);

    // 7
    glBindBuffer(GL_ARRAY_BUFFER, 0);// 设置当前buffer为0 即解绑


}

extern "C"
JNIEXPORT void JNICALL
Java_com_wisky_hellotriangle_HelloTriRender_drawFrame(JNIEnv *env, jobject thiz) {
    Init();
}

