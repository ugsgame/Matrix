#ifndef __SCRIPTBIND_COCOSDENSHION__
#define __SCRIPTBIND_COCOSDENSHION__

#include "MatrixMono.h"

class ScriptBind_CocosDenshion:public IMonoScriptBind
{
public:
	virtual const char* GetNamespace(){ return "MatrixEngine.CocosDenshion.Native";}
};

#endif