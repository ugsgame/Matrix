LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := matrixmono_static

LOCAL_MODULE_FILENAME := libmatrixmono

MY_FILES_PATH  :=  $(LOCAL_PATH)/Classes 

MY_FILES_SUFFIX := %.cpp %.c %.cc

My_All_Files := $(foreach src_path,$(MY_FILES_PATH), $(shell find $(src_path)/.) ) 
My_All_Files := $(My_All_Files:$(MY_CPP_PATH)/./%=$(MY_CPP_PATH)%)
MY_CPP_LIST  := $(filter $(MY_FILES_SUFFIX),$(My_All_Files)) 
MY_CPP_LIST  := $(MY_CPP_LIST:$(LOCAL_PATH)/%=%)

LOCAL_SRC_FILES := $(MY_CPP_LIST)

LOCAL_C_INCLUDES := $(LOCAL_PATH)/Classes \
				$(LOCAL_PATH)/Headers 

LOCAL_LDLIBS := -lGLESv2 \
                -llog \
                -lz

LOCAL_EXPORT_LDLIBS := -lGLESv2 \
                       -llog \
                       -lz


# define the macro to compile through support/zip_support/ioapi.c
LOCAL_CFLAGS := -Wno-psabi -DUSE_FILE32API -std=c++11
LOCAL_EXPORT_CFLAGS := -Wno-psabi -DUSE_FILE32API -std=c++11

include $(BUILD_STATIC_LIBRARY)
