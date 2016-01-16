#include <jni.h>
#include <stdio.h>
#include "Common.h"
#include "Native.cpp"

int registerNativeMethod(JNIEnv *env);
int LoadDexFile(JNIEnv* env);
static jobject getAppContext(JNIEnv *env);
static jobject getClassLoader(jobject japplication);
jclass getClassForName(JNIEnv *env, char *classPathName);

// XXX 0x1 编写JNI_OnLoad
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {

	JNIEnv* env = NULL;
	jint result = -1;

	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		LOGE("ERROR: env is null");
		return result;
	}

	// 注册JNI方法
	if(JNI::registerNativeMethod(env)){
		LOGE("ERROR: native method registration failed");
		return JNI_ERR;

	}
	LOGD("JNI_OnLoad complete.");
	return JNI_VERSION_1_4;
}

