
//#include "stdneb.h"
#include "cocos2d.h"

#include "ScriptBind_System.h"

USING_NS_CC;

#ifdef WIN32

#define MAX_LEN         (cocos2d::kMaxLogLen + 1)

void CCPrintf(const char * pszFormat, ...)
{
	char szBuf[MAX_LEN];

	va_list ap;
	va_start(ap, pszFormat);
	vsnprintf_s(szBuf, MAX_LEN, MAX_LEN, pszFormat, ap);
	va_end(ap);

	WCHAR wszBuf[MAX_LEN] = {0};
	MultiByteToWideChar(CP_UTF8, 0, szBuf, -1, wszBuf, sizeof(wszBuf));
	OutputDebugStringW(wszBuf);
	OutputDebugStringA("\n");

	WideCharToMultiByte(CP_ACP, 0, wszBuf, sizeof(wszBuf), szBuf, sizeof(szBuf), NULL, FALSE);
	printf("%s", szBuf);
}
#else
void CCPrintf(const char * pszFormat, ...)
{
	//TODO:ndk 编译会出错不知是什么问题
	//CCLog(pszFormat,...);
}
#endif


ScriptBind_System::ScriptBind_System()
{
	REGISTER_METHOD(Log);
	REGISTER_METHOD(LogWaring);
	REGISTER_METHOD(LogError);
	REGISTER_METHOD(LogFile);
}
ScriptBind_System::~ScriptBind_System()
{
	
}


void ScriptBind_System::Log(mono::string log,bool line)
{
	//TODO
	if(line)
		CCLog(ToMatrixString(log).c_str());
	else
		CCPrintf(ToMatrixString(log).c_str());

}

void ScriptBind_System::LogWaring(mono::string log)
{
	//TODO
	CCLog("Waring:%s",ToMatrixString(log).c_str());
}

void ScriptBind_System::LogError(mono::string log)
{
	//TODO
	CCLog("Error:%s",ToMatrixString(log).c_str());
}

void ScriptBind_System::LogFile(mono::string log)
{
	//TODO
}