
#ifndef ANDROIDHELPER_H_
#define ANDROIDHELPER_H_

namespace AndroidHepler{
	static int exception_check_and_clear(JNIEnv *env) {
		if (env->ExceptionCheck()) {
			env->ExceptionDescribe();
			env->ExceptionClear();
			return -1;
		}
		return 0;
	}
	bool invoke_static_object_method(JNIEnv* env, jobject *result,
			const char *className, const char *methodSig, const char *methodName,
			...) {
		jclass clazz = env->FindClass(className);
		exception_check_and_clear(env);
		if (!clazz) {
			LOGE("invoke static object method: %s->%s type:%s error��", className, methodName, methodSig);
			return false;
		}
		jmethodID jmethodId_ = env->GetStaticMethodID(clazz, methodName, methodSig);
		exception_check_and_clear(env);

		if (!jmethodId_) {
			env->DeleteLocalRef(clazz);
			LOGE("invoke static object method: %s->%s type:%s error��", className, methodName, methodSig);
			return false;
		}
		va_list arg_ptr;

		va_start(arg_ptr, methodName);
		*result = env->CallStaticObjectMethodV(clazz, jmethodId_, arg_ptr);
		exception_check_and_clear(env);
		va_end(arg_ptr);
		env->DeleteLocalRef(clazz);

		return true;
	}
	bool invoke_object_method(JNIEnv* env, jobject *result, const char *className,
			jobject jobj, const char *methodSig, const char *methodName, ...) {

		jclass clazz = env->FindClass(className);
		exception_check_and_clear(env);

		if (!clazz || !jobj) {
			LOGE("invoke object method: %s->%s type:%s error��", className, methodName, methodSig);
			return false;
		}

		jmethodID jmethodId_ = env->GetMethodID(clazz, methodName,
				methodSig);
		exception_check_and_clear(env);
		if (!jmethodId_) {
			env->DeleteLocalRef(clazz);
			LOGE("invoke object method: %s->%s type:%s error��", className, methodName, methodSig);
			return false;
		}
		va_list arg_ptr;
		va_start(arg_ptr, methodName);
		*result = env->CallObjectMethodV(jobj, jmethodId_, arg_ptr);
		exception_check_and_clear(env);

		va_end(arg_ptr);
		env->DeleteLocalRef(clazz);
		return true;
	}

	static jobject getAppContext(JNIEnv *env) {
		jobject jCurrentActivityThread = NULL;
		bool okey = false;
		okey = invoke_static_object_method(env, &jCurrentActivityThread,
				"android/app/ActivityThread", "()Landroid/app/ActivityThread;",
				"currentActivityThread");
		if (!okey || !jCurrentActivityThread) {
			LOGE("jCurrentActivityThread == null");
			return NULL;
		}

		jobject japplication = NULL;
		okey = invoke_object_method(env, &japplication,
				"android/app/ActivityThread", jCurrentActivityThread,
				"()Landroid/app/Application;", "getApplication");

		if (!okey || !japplication) {
			LOGE("japplication == NULL");
			return NULL;
		}
		return japplication;
	}

	static jobject getClassLoader(JNIEnv *env, jobject japplication){
		bool okey = false;
		jobject mClassLoader;
		okey = invoke_object_method(env, &mClassLoader,
				"android/app/Application", japplication,
				"()Ljava/lang/ClassLoader;", "getClassLoader");

		if (!okey || !japplication) {
			LOGE("japplication == NULL");
			return NULL;
		}
		return mClassLoader;
	}

	static jclass getClassForName(JNIEnv *env, jstring className){
		jobject jCurrentActivityThread = NULL;
		bool okey = false;
		okey = invoke_static_object_method(env, &jCurrentActivityThread,
				"android/app/ActivityThread", "()Landroid/app/ActivityThread;",
				"currentActivityThread");
		if (!okey || !jCurrentActivityThread) {
			LOGE("jCurrentActivityThread == null");
			return NULL;
		}
	}
}
#endif /* ANDROIDHELPER_H_ */
