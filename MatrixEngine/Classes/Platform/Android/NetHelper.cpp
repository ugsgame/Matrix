
#include "Platform/NetHelper.h"

#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include "platform/android/jni/JniHelper.h"

#include <jni.h>
#include <android/log.h>


static NetHelper *s_SharedNetHelper = NULL;

#define  CLASS_NAME "org/fonle/matrix/jni/NetHelper"

void OpenUrl(const std::string& key) {
	JniMethodInfo minfo;
	jstring jkey = 0, jrtn = 0;

	if (false == JniHelper::getStaticMethodInfo(minfo, CLASS_NAME, "OpenUrl", "(Ljava/lang/String;)V"))
		return;

	jkey = minfo.env->NewStringUTF(key.c_str());
	jrtn = (jstring)minfo.env->CallStaticObjectMethod(minfo.classID, minfo.methodID, jkey);

	minfo.env->DeleteLocalRef(jkey);
}

NetHelper::NetHelper()
{

}

NetHelper::~NetHelper()
{

}

void NetHelper::openUrl(const char* url)
{
	OpenUrl(std::string(url));
}

NetHelper* NetHelper::shareNetHelper()
{
	if(!s_SharedNetHelper)
	{
		s_SharedNetHelper = new NetHelper();
	}
	return s_SharedNetHelper;
}

#endif