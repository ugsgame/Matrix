
#ifndef __TESTPLUGIN__
#define __TESTPLUGIN__

#include "PluginSystem/MatrixPlugin.h"

#define TESTPLUGIN_NAME			"TestPlugin"
#define	TESTPLUGIN_NAMESPACE	"Game.Plugins"


extern "C" EXPORT_DLL MatrixPlugin*  CreatePlugin();

class TestPlugin:public MatrixPlugin
{
public:
	TestPlugin();
	~TestPlugin();

public:
	static TestPlugin* create();

	virtual bool setupPlugin(bool debug);
	virtual void registerScript();

protected:
private:
};

#endif
