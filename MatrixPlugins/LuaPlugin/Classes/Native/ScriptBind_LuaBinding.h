
#ifndef __SCRIPTBINDING_TESTPLUGIN__
#define __SCRIPTBINDING_TESTPLUGIN__

#include "MatrixMono.h"
#include "LuaPlugin.h"


class ScriptBind_LuaBinding:public IMonoScriptBind
{
public:

	virtual const char* GetNamespace()
	{
		return PLUGIN_NAMESPACE;
	}

	virtual const char* GetClassName()
	{
		return PLUGIN_NAME;
	}
public:

	ScriptBind_LuaBinding();
	~ScriptBind_LuaBinding();

	static void Test();

protected:
private:
};

#endif