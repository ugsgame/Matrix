#ifndef __SCRIPTBIND_RICHCONTROLS__
#define __SCRIPTBIND_RICHCONTROLS__

#include "MatrixMono.h"

class ScriptBind_RichControls:public IMonoScriptBind
{
public:
	virtual const char* GetNamespace(){ return "MatrixEngine.RichControls.Native";}
};

#endif