#include <jni.h>
#include <string>

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

#include <android/log.h>





static void checkGlError(const char *op) {
    for (GLint error = glGetError(); error; error = glGetError()) {
//        LOGI("after %s() glError (0x%x)\n", op, error);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wisky_nativeopenglbg_GLES3Render_surfaceChanged(JNIEnv *env, jobject thiz, jint w, jint h) {

    //设置程序窗口
    glViewport(0, 0, w, h);
    checkGlError("glViewport");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wisky_nativeopenglbg_GLES3Render_drawFrame(JNIEnv *env, jobject thiz) {

    //清空颜色缓冲区
    glClear(GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");

}

extern "C"
JNIEXPORT void JNICALL
Java_com_wisky_nativeopenglbg_GLES3Render_surfaceCreated(JNIEnv *env, jobject thiz) {

    glClearColor(1, 0,0, 1.0f);
    checkGlError("glClearColor");

}