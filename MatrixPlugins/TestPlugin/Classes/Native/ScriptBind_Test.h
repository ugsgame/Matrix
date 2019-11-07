
#ifndef __SCRIPTBINDING_TESTPLUGIN__
#define __SCRIPTBINDING_TESTPLUGIN__

#include "MatrixMono.h"
#include "TestPlugin.h"


class ScriptBind_Test:public IMonoScriptBind
{
public:

	virtual const char* GetNamespace()
	{
		return TESTPLUGIN_NAMESPACE;
	}

	virtual const char* GetClassName()
	{
		return TESTPLUGIN_NAME;
	}
public:

	ScriptBind_Test();
	~ScriptBind_Test();

	static void Test();

protected:
private:
};

#endif