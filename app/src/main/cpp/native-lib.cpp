#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_googleplay_itheima_com_googleplay_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
