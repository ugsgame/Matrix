
#ifndef __LUAPLUGIN__
#define __LUAPLUGIN__

#include "PluginSystem/MatrixPlugin.h"

#define PLUGIN_NAME			"LuaPlugin"
#define	PLUGIN_NAMESPACE	"Game.Plugins"


extern "C" EXPORT_DLL MatrixPlugin*  CreatePlugin();

class LuaPlugin:public MatrixPlugin
{
public:
	LuaPlugin();
	~LuaPlugin();

public:
	static LuaPlugin* create();

	virtual bool setupPlugin(bool debug);
	virtual void registerScript();

protected:
private:
};

#endif
