LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := libdex/CmdUtils.cpp \
	libdex/DexCatch.cpp \
	libdex/DexClass.cpp \
	libdex/DexDataMap.cpp \
	libdex/DexDebugInfo.cpp \
	libdex/DexFile.cpp \
	libdex/DexInlines.cpp \
	libdex/DexOptData.cpp \
	libdex/DexOpcodes.cpp \
	libdex/DexProto.cpp \
	libdex/DexSwapVerify.cpp \
	libdex/DexUtf.cpp \
	libdex/InstrUtils.cpp \
	libdex/Leb128.cpp \
	libdex/OptInvocation.cpp \
	libdex/sha1.cpp \
	libdex/SysUtil.cpp \
	libdex/ZipArchive.cpp \
	libdex/safe_iop.c
LOCAL_MODULE := libdex
LOCAL_LDLIBS += -llog
include $(BUILD_STATIC_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := ClassLoaderShell
LOCAL_STATIC_LIBRARIES := libdex
LOCAL_LDLIBS	:= -llog -ldvm -lz
LOCAL_SRC_FILES := Main.cpp JstringUtil.cpp

include $(BUILD_SHARED_LIBRARY)
