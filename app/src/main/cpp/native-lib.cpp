#include <jni.h>
#include <string>
extern "C"{
    extern int p_main(int argc, const char *argv[]);
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_liyaan_bsdiff_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT void JNICALL
Java_com_liyaan_bsdiff_BsPatcher_bsPatcher(JNIEnv *env, jobject thiz, jstring old_apk_,
                                           jstring patch_, jstring output_) {
    const char *old_apk = env->GetStringUTFChars(old_apk_,0);
    const char *patch = env->GetStringUTFChars(patch_,0);
    const char *output = env->GetStringUTFChars(output_,0);
    printf("%s",patch);
    const char *argv[] = {"",old_apk,output,patch};
    p_main(4,argv);
    env->ReleaseStringUTFChars(old_apk_,old_apk);
    env->ReleaseStringUTFChars(patch_,patch);
    env->ReleaseStringUTFChars(output_,output);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_liyaan_bsdiff_BsPatcher_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}