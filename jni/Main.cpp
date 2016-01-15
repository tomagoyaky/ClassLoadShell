#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>	// PATH_MAX
#include <fcntl.h>  // open()
#include <vm/DvmDex.h>
#include <libdex/DexFile.h>
#include "Common.h"
#include "AndroidHelper.h"

#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
const char *gClassPathName = "com/tomagoyaky/TestUtil";
const char *gNativeBridgeClassPathName = "com/tomagoyaky/MainActivity";
const char *gDexFilePath = "/sdcard/classes.dex";
DvmDex *pDvmDex;
jobject jgApplication;
jobject jgClassLoader;

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

	// 加载Dex文件
	if(LoadDexFile(env)){
		LOGE("ERROR: load dex file failed");
		return result;
	}

	// 注册JNI方法
	if(registerNativeMethod(env)){
		LOGE("ERROR: native method registration failed");
		return JNI_ERR;

	}
	LOGD("JNI_OnLoad complete.");
	return JNI_VERSION_1_4;
}

jstring invokeDexMethod(JNIEnv* env, jstring msg){

	/*
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.toString();
	 * */
	jclass jcStringBuilder = env->FindClass("java/lang/StringBuilder");
	jmethodID mStringBuilder_init = env->GetMethodID(jcStringBuilder, "<init>", "()V");
	jobject joStringBuilder = env->NewObject(jcStringBuilder, mStringBuilder_init);

	jmethodID mStringBuilder_append = env->GetMethodID(jcStringBuilder, "append", "(Ljava/lang/String;)java/lang/StringBuilder");
	jmethodID mStringBuilder_toString = env->GetMethodID(jcStringBuilder, "toString", "()Ljava/lang/String;");
	joStringBuilder = env->CallObjectMethod(joStringBuilder, mStringBuilder_append, "aaaaaaaaaaaa");
	joStringBuilder = env->CallObjectMethod(joStringBuilder, mStringBuilder_append, "sssssssssssssssss");
	jstring result = (jstring)env->CallObjectMethod(joStringBuilder, mStringBuilder_toString);

//	LOGD("msg:%s, len:%d", Jstring2CStr(env, msg, charLen), *charLen);
	return result;
}

// XXX 0x2 获取dex文件
int LoadDexFile(JNIEnv* env){

	int result = -1;
	jstring jDexFilePath = env->NewStringUTF(gDexFilePath);

	if(access(gDexFilePath, R_OK) == -1){
		LOGE("dex file {%s} is not exist.", gDexFilePath);
		return result;
	}

	int fd = open(gDexFilePath, O_RDONLY);
	LOGD("fd:%d", fd);
	if(fd != -1){
		result = dvmDexFileOpenFromFd(fd, &pDvmDex);
	}

	LOGD("load dex file %s, pDvmDex->pDexFile->baseAddr:%p, result:%d", gDexFilePath, pDvmDex->pDexFile->baseAddr, result);
	return result;
}

// XXX 0x3 注册本地方法
const JNINativeMethod method_table[] = {
		{"invokeRemoteNativeDexMethod", "(Ljava/lang/String;)Ljava/lang/String;", (void*)invokeDexMethod}
//    {"setVal_native", "(I)V", (void*)hello_setVal},
//    {"getVal_native", "()I", (void*)hello_getVal},
};

int registerNativeMethod(JNIEnv *env){

	// 获取类
	jclass clazz = env->FindClass(gNativeBridgeClassPathName);

	if (clazz == NULL){
		LOGE("ERROR: unable to find class '%s'", (char*)gNativeBridgeClassPathName);
		return JNI_ERR;
	}
	return env->RegisterNatives(clazz, method_table, NELEM(method_table));
}
