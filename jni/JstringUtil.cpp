/*
 * JstringUtil.cpp
 *
 *  Created on: 2016年1月16日
 *      Author: peng
 */
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "Common.h"

void DebugMem(void * buf, int len, unsigned char more) {
	unsigned char * curaddr;
	int i;
	int left;
	int sector = 0;

//	if(len > 0x20){
//		len = 0x30;
//	}
	if(buf == NULL){
		return;
	}
	left = len;
	char msgbuf[1024] = {0};
	curaddr = (unsigned char*) buf;

	while (left > 0) {

		if (left < 16) {
			int cnt = sprintf(msgbuf, "%08Xh  ", (unsigned int) curaddr);

			for (i = 0; i < left; i++) {
				cnt += sprintf(msgbuf + cnt, "%02X ", *(curaddr + i));
			}

			for (i = left; i < 16; i++)
				cnt += sprintf(msgbuf + cnt, "   ");

			for (i = 0; i < left; i++) {
				if ((*(curaddr + i) >= '!') && (*(curaddr + i) <= '~'))
					cnt += sprintf(msgbuf + cnt, "%c", *(curaddr + i));
				else
					cnt += sprintf(msgbuf + cnt, ".");
			}
			left -= 16;
			LOGV("%s", msgbuf);
		} else {

			int cnt = sprintf(msgbuf, "%08Xh  ", (unsigned int) curaddr);

			for (i = 0; i < 16; i++) {
				cnt += sprintf(msgbuf + cnt, "%02X ", *(curaddr + i));
			}

			for (i = 0; i < 16; i++) {
				if ((*(curaddr + i) >= '!') && (*(curaddr + i) <= '~'))
					cnt += sprintf(msgbuf + cnt, "%c", *(curaddr + i));
				else
					cnt += sprintf(msgbuf + cnt, ".");
			}
			LOGV("%s", msgbuf);
		}

		left -= 16;
		sector += 16;
		curaddr += 16;
	}
}

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


