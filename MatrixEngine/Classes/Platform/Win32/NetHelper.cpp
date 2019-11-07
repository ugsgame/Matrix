
#include "Platform/NetHelper.h"

#if(CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
 
static NetHelper *s_SharedNetHelper = NULL;

NetHelper::NetHelper()
{

}

NetHelper::~NetHelper()
{

}

void NetHelper::openUrl(const char* url)
{

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