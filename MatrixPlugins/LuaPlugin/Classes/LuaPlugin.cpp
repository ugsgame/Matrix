
#include "LuaPlugin.h"
#include "Native/ScriptBind_LuaBinding.h"

extern "C"
{
	MatrixPlugin* CreatePlugin()
	{
		return LuaPlugin::create();
	}

}

LuaPlugin::LuaPlugin()
{
	this->setPluginName(PLUGIN_NAME);
	this->setNamespace(PLUGIN_NAMESPACE);
}

LuaPlugin::~LuaPlugin()
{

}


LuaPlugin* LuaPlugin::create()
{
	return new LuaPlugin();
}

bool LuaPlugin::setupPlugin(bool debug)
{
	this->registerScript();

	return true;
}

void LuaPlugin::registerScript()
{
	RegisterBinding(ScriptBind_LuaBinding);
}
