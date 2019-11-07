
#ifndef __SCRIPTBIND_NETHELPER__
#define __SCRIPTBIND_NETHELPER__

#include "ScriptBind_Matrix.h"

class ScriptBind_NetHelper:public ScriptBind_Matrix
{
public:
	ScriptBind_NetHelper();
	~ScriptBind_NetHelper();

	virtual const char*	GetClassName(){ return "NativeNetHelper";}

protected:

	static void OpenUrl(mono::string url);

private:
};


#endif