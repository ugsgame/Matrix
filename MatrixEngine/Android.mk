LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := matrixengine_static

LOCAL_MODULE_FILENAME := libmatrixengine

MY_FILES_PATH  :=  $(LOCAL_PATH)/Classes 

MY_FILES_SUFFIX := %.cpp %.c %.cc

My_All_Files := $(foreach src_path,$(MY_FILES_PATH), $(shell find $(src_path)/.) ) 
My_All_Files := $(My_All_Files:$(MY_CPP_PATH)/./%=$(MY_CPP_PATH)%)
MY_CPP_LIST  := $(filter $(MY_FILES_SUFFIX),$(My_All_Files)) 
MY_CPP_LIST  := $(MY_CPP_LIST:$(LOCAL_PATH)/%=%)

LOCAL_SRC_FILES := $(MY_CPP_LIST)

LOCAL_C_INCLUDES := $(LOCAL_PATH)/Classes \
				$(LOCAL_PATH)/../MatrixMono/Headers \
				$(LOCAL_PATH)/../cocos2d-x-2.2.3/cocos2dx 

LOCAL_LDLIBS := -lGLESv2 \
                -llog \
                -lz

LOCAL_EXPORT_LDLIBS := -lGLESv2 \
                       -llog \
                       -lz

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocosdenshion_static
LOCAL_WHOLE_STATIC_LIBRARIES += box2d_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocos_extension_static


# define the macro to compile through support/zip_support/ioapi.c
LOCAL_CFLAGS := -Wno-psabi -DUSE_FILE32API -fpermissive
LOCAL_EXPORT_CFLAGS := -Wno-psabi -DUSE_FILE32API

include $(BUILD_STATIC_LIBRARY)

$(call import-module,cocos2dx)
$(call import-module,CocosDenshion/android)
$(call import-module,extensions)
$(call import-module,external/Box2D)