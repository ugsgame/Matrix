#include "cocos2d.h"
#include "AndroidJNIHelper.h"

USING_NS_CC;

#ifdef ANDROID
#include "platform/android/jni/JniHelper.h"

#include <string.h>
#include <android/log.h>

#if 1
#define  LOG_TAG    "AndroidJNIHelper"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...) 
#endif

#define JAVAVM    cocos2d::JniHelper::getJavaVM()

extern "C"
{
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
}


AndroidJNIHelper::AndroidJNIHelper()
{
	REGISTER_METHOD(GetMethodID);
	REGISTER_METHOD(GetConstructorID);
}

AndroidJNIHelper::~AndroidJNIHelper()
{

}


jmethodID AndroidJNIHelper::GetMethodID(jclass clazz,mono::string name,mono::string sig, bool isStatic)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jmethodID jmtd;
	if(isStatic)
	{
		jmtd = env->functions->GetStaticMethodID(env,clazz,ToMatrixString(name).c_str(),ToMatrixString(sig).c_str());
	} 
	else
	{
		jmtd = env->functions->GetMethodID(env,clazz,ToMatrixString(name).c_str(),ToMatrixString(sig).c_str());
	}

	return jmtd;
}

jmethodID AndroidJNIHelper::GetConstructorID(jclass clazz,mono::string sig)
{
	JNIEnv * env;
	CCAssert(getEnv(&env),"Can't get env!!");
	jmethodID jmtd = env->functions->GetMethodID(env,clazz,"<init>",ToMatrixString(sig).c_str());
	return jmtd;
}


#endif