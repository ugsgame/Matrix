
#ifndef __SCRIPTBIND_COCOS2D__
#define __SCRIPTBIND_COCOS2D__

#include "MatrixMono.h"

class ScriptBind_Cocos2d:public IMonoScriptBind
{
public:
	virtual const char* GetNamespace(){ return "MatrixEngine.Cocos2d.Native";}
};

#endif