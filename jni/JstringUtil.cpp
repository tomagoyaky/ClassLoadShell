/*
 * JstringUtil.cpp
 *
 *  Created on: 2016年1月16日
 *      Author: peng
 */

#include <stdlib.h>
#include <jni.h>
char* Jstring2CStr(JNIEnv* env, jstring jstr, int* charLen) {
	char* rtn = NULL;
	jclass clsstring = env->FindClass("java/lang/String");
	jstring strencode = env->NewStringUTF("UTF-8");

	jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
	jsize alen = env->GetArrayLength(barr);
	*charLen = alen + 1;
	jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	env->ReleaseByteArrayElements(barr, ba, 0);
	return rtn;
}

jstring CStr2Jstring(JNIEnv* env, const char* pat) {
	jclass strClass = (env)->FindClass("Ljava/lang/String;");
	jmethodID ctorID = (env)->GetMethodID(strClass, "<init>",
			"([BLjava/lang/String;)V");
	jbyteArray bytes = (env)->NewByteArray((jsize) strlen(pat));
	(env)->SetByteArrayRegion(bytes, 0, (jsize) strlen(pat), (jbyte*) pat);
	jstring encoding = (env)->NewStringUTF("UTF-8");
	return (jstring) (env)->NewObject(strClass, ctorID, bytes, "UTF-8");
}


