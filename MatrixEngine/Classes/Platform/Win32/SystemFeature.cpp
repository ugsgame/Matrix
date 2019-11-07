
#include "Platform/SystemFeature.h"

#if(CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)

static SystemFeature* s_SharedSystemFeature = NULL;

SystemFeature::SystemFeature()
{

}

SystemFeature::~SystemFeature()
{

}

const char* SystemFeature::getSDPath()
{
	return CCFileUtils::sharedFileUtils()->getWritablePath().c_str();
}

bool SystemFeature::isSDExist()
{
	return true;
}

const char* SystemFeature::getPackageName()
{
	return "";
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

