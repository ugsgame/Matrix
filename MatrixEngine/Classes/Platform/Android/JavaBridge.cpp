
#include "cocos2d.h"
#include "JavaBridge.h"
#include "Platform/NativeBridge.h"

#include "AndroidJNIHelper.h"
#include "AndroidJNI.h"

USING_NS_CC;
#include "platform/android/jni/JniHelper.h"

#include <string.h>
#include <android/log.h>
#include <jni.h>

#if 1
#define  LOG_TAG    "JavaBridge"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...) 
#endif

#define JAVAVM    cocos2d::JniHelper::getJavaVM()


extern "C" {

	static pthread_key_t s_threadKey;

	static void detach_current_thread (void *env) {
		JAVAVM->DetachCurrentThread();
	}

	static bool getEnv(JNIEnv **env)
	{
		bool bRet = false;

		switch(JAVAVM->GetEnv((void**)env, JNI_VERSION_1_4))
		{
		case JNI_OK:
			bRet = true;
			break;
		case JNI_EDETACHED:
			pthread_key_create (&s_threadKey, detach_current_thread);
			if (JAVAVM->AttachCurrentThread(env, 0) < 0)
			{
				LOGD("Failed to get the environment using AttachCurrentThread()");
				break;
			}
			if (pthread_getspecific(s_threadKey) == NULL)
				pthread_setspecific(s_threadKey, env); 
			bRet = true;
			break;
		default:
			LOGD("Failed to get the environment using GetEnv()");
			break;
		}      

		return bRet;
	}

	static std::string jstring2string_(jstring jstr)
	{
		if (jstr == NULL)
		{
			return "";
		}

		JNIEnv *env = 0;

		if (! getEnv(&env))
		{
			return 0;
		}

		const char* chars = env->GetStringUTFChars(jstr, NULL);
		std::string ret(chars);
		env->ReleaseStringUTFChars(jstr, chars);

		return ret;
	}
	static mono::string jstring2monostring_(jstring jstr)
	{
		if (jstr == NULL)
		{
			return ToMonoString("");
		}

		JNIEnv *env = 0;

		if (! getEnv(&env))
		{
			return 0;
		}

		const char* chars = env->GetStringUTFChars(jstr, NULL);
		mono::string ret = ToMonoString(chars);

		env->ReleaseStringUTFChars(jstr, chars);

		return ret;
	}


	JNIEXPORT void JNICALL Java_com_matrix_player_CSharpBridge_nativeOnCallStaticVoidFuctionVoid(JNIEnv* env, jobject thiz, jstring fuc,jstring klass,jstring nameSpace) {
		
		std::string fuc_ = jstring2string_(fuc);
		std::string class_ = jstring2string_(klass);
		std::string nameSpace_ = jstring2string_(nameSpace);

		NativeBridge::ShareNativeBridge()->CallStaticVoidFuction(fuc_,class_,nameSpace_);
	}

	JNIEXPORT void JNICALL Java_com_matrix_player_CSharpBridge_nativeOnCallStaticVoidFuctionInt(JNIEnv* env, jobject thiz, jstring fuc,jstring klass,jstring nameSpace,jint parm) {

		std::string fuc_ = jstring2string_(fuc);
		std::string class_ = jstring2string_(klass);
		std::string nameSpace_ = jstring2string_(nameSpace);

		NativeBridge::ShareNativeBridge()->CallStaticVoidFuction(fuc_,class_,nameSpace_,parm);
	}

	JNIEXPORT void JNICALL Java_com_matrix_player_CSharpBridge_nativeOnCallStaticVoidFuctionString(JNIEnv* env, jobject thiz, jstring fuc,jstring klass,jstring nameSpace,jstring parm) {

		std::string fuc_ = jstring2string_(fuc);
		std::string class_ = jstring2string_(klass);
		std::string nameSpace_ = jstring2string_(nameSpace);
		std::string parm_ = jstring2string_(parm);

		NativeBridge::ShareNativeBridge()->CallStaticVoidFuction(fuc_,class_,nameSpace_,parm_);
	}

	JNIEXPORT void JNICALL Java_com_matrix_player_CSharpBridge_nativeOnCallStaticVoidFuctionBoolean(JNIEnv* env, jobject thiz, jstring fuc,jstring klass,jstring nameSpace,jboolean parm) {

		std::string fuc_ = jstring2string_(fuc);
		std::string class_ = jstring2string_(klass);
		std::string nameSpace_ = jstring2string_(nameSpace);

		NativeBridge::ShareNativeBridge()->CallStaticVoidFuction(fuc_,class_,nameSpace_,(bool)parm);
	}


	JNIEXPORT void JNICALL Java_com_matrix_player_CSharpBridge_nativeOnCallStaticVoidFuctionBooleanString(JNIEnv* env, jobject thiz, jstring fuc,jstring klass,jstring nameSpace,jboolean parm1,jstring parm2) {

		std::string fuc_ = jstring2string_(fuc);
		std::string class_ = jstring2string_(klass);
		std::string nameSpace_ = jstring2string_(nameSpace);
		std::string parm2_ = jstring2string_(parm2);

		NativeBridge::ShareNativeBridge()->CallStaticVoidFuction(fuc_,class_,nameSpace_,(bool)parm1,parm2_);
	}

	JNIEXPORT jint JNICALL Java_com_matrix_player_CSharpBridge_nativeOnCallStaticIntFuctionVoid(JNIEnv* env, jobject thiz, jstring fuc,jstring klass,jstring nameSpace) {

		std::string fuc_ = jstring2string_(fuc);
		std::string class_ = jstring2string_(klass);
		std::string nameSpace_ = jstring2string_(nameSpace);

		return NativeBridge::ShareNativeBridge()->CallStaticIntFuction(fuc_,class_,nameSpace_);
	}
}