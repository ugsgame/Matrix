
#include "Platform/SystemFeature.h"

#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include "platform/android/jni/JniHelper.h"

#include <jni.h>

static SystemFeature* s_SharedSystemFeature = NULL;

#define  CLASS_NAME "org/fonle/matrix/jni/SystemFeature"

SystemFeature::SystemFeature()
{

} 

SystemFeature::~SystemFeature()
{

}

const char* SystemFeature::getSDPath()
{
	//std::string result;
	jstring jrtn = 0;
	JniMethodInfo minfo;

	if (false == JniHelper::getStaticMethodInfo(minfo, CLASS_NAME, "GetSDPath", "()Ljava/lang/String;"))
		return;

	jrtn = (jstring)minfo.env->CallStaticObjectMethod(minfo.classID, minfo.methodID);

	const char *rtn = minfo.env->GetStringUTFChars(jrtn, 0);
	//result = rtn;
	//minfo.env->ReleaseStringUTFChars(jrtn, rtn);

	return rtn;
}

bool SystemFeature::isSDExist()
{
	JniMethodInfo minfo;

	if (false == JniHelper::getStaticMethodInfo(minfo, CLASS_NAME, "IsSDExist", "()Z"))
		return false;

	return minfo.env->CallStaticObjectMethod(minfo.classID, minfo.methodID);
}

const char* SystemFeature::getPackageName()
{
	JniMethodInfo minfo;
	jstring jrtn = 0;
	if (false == JniHelper::getStaticMethodInfo(minfo, CLASS_NAME, "GetPackageName", "()Ljava/lang/String;"))
		return;

	jrtn = (jstring)minfo.env->CallStaticObjectMethod(minfo.classID, minfo.methodID);
	const char *rtn = minfo.env->GetStringUTFChars(jrtn, 0);
	return rtn;
}

SystemFeature* SystemFeature::shareSystemFeature()
{
	if(!s_SharedSystemFeature)
	{
		s_SharedSystemFeature = new SystemFeature();
	}
	return s_SharedSystemFeature;
}

#endif