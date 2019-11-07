
#ifndef  __SCRIPTBIND_MATRIX__
#define  __SCRIPTBIND_MATRIX__

#include "MatrixMono.h"

class ScriptBind_Matrix:public IMonoScriptBind
{
public: 
	virtual const char* GetNamespace(){ return "MatrixEngine.Native";}

};

#endif