#ifndef __SCRIPTBIND_COCOSTUDIO__
#define __SCRIPTBIND_COCOSTUDIO__

#include "MatrixMono.h"

class ScriptBind_CocoStudio:public IMonoScriptBind
{
public:
	virtual const char* GetNamespace(){ return "MatrixEngine.CocoStudio.Native";}
};

#endif