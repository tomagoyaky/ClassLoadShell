LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ClassLoaderShell
LOCAL_SRC_FILES := Main.cpp

LOCAL_LDLIBS	:= -llog -ldvm

include $(BUILD_SHARED_LIBRARY)
