
#ifndef __SCRIPTBIND_SYSTEM__
#define __SCRIPTBIND_SYSTEM__

#include "ScriptBind_Matrix.h"

class ScriptBind_System:public ScriptBind_Matrix
{
public:
	ScriptBind_System();
	~ScriptBind_System();

	virtual const char*	GetClassName(){ return "NativeSystem";}

	static void Log(mono::string log,bool line);

	static void LogWaring(mono::string log);

	static void LogError(mono::string log);

	static void LogFile(mono::string log);
};

#endif