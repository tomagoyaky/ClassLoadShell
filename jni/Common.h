/*
 * Common.h
 *
 *  Created on: 2016年1月16日
 *      Author: peng
 */

#ifndef COMMON_H_
#define COMMON_H_
#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>
#include <cutils/log.h>
#define DEBUG 1

#undef 	LOG_TAG
#define LOG_TAG "tomagoyaky_native"

#if DEBUG
#define LOGD(fmt,...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "[%s]" fmt, __FUNCTION__,##__VA_ARGS__)
#define LOGI(fmt,...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "[%s]" fmt, __FUNCTION__,##__VA_ARGS__)
#define LOGV(fmt,...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, "[%s]" fmt, __FUNCTION__,##__VA_ARGS__)
#define LOGW(fmt,...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "[%s]" fmt, __FUNCTION__,##__VA_ARGS__)
#define LOGE(fmt,...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "[%s]" fmt, __FUNCTION__,##__VA_ARGS__)
#define LOGF(fmt,...) __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, "[%s]" fmt, __FUNCTION__,##__VA_ARGS__)
#define assert(x) \
    ((x) ? ((void)0) : (ALOGE("ASSERT FAILED (%s:%d): %s", \
        __FILE__, __LINE__, #x), *(int*)39=39, (void)0) )
#else
#define LOGD(...) while(0){}
#define LOGI(...) while(0){}
#define LOGV(...) while(0){}
#define LOGW(...) while(0){}
#define LOGE(...) while(0){}
#define LOGW(...) while(0){}
#endif

/*
 * These match the definitions in the VM specification.
 */
typedef uint8_t             u1;
typedef uint16_t            u2;
typedef uint32_t            u4;
typedef uint64_t            u8;
typedef int8_t              s1;
typedef int16_t             s2;
typedef int32_t             s4;
typedef int64_t             s8;

char* Jstring2CStr(JNIEnv* env, jstring jstr, int* charLen);
jstring CStr2Jstring(JNIEnv* env, const char* pat);

#endif /* COMMON_H_ */
