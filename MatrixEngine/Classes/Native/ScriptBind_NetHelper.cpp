
#include "cocos2d.h"

#include "ScriptBind_NetHelper.h"
#include "Platform/NetHelper.h"

ScriptBind_NetHelper::ScriptBind_NetHelper()
{
	REGISTER_METHOD(OpenUrl);
}

ScriptBind_NetHelper::~ScriptBind_NetHelper()
{

}

void ScriptBind_NetHelper::OpenUrl(mono::string url)
{
	NetHelper::shareNetHelper()->openUrl(ToMatrixString(url).c_str());
}