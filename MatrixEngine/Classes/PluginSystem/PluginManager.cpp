
#include "PluginManager.h"
#include "MatrixPlugin.h"

//TODO Better

PluginManager* PluginManager::_sharePluginManager = NULL;

PluginManager::PluginManager()
{

}

PluginManager::~PluginManager()
{

}

PluginManager* PluginManager::sharePluginManager()
{
	if (!_sharePluginManager)
	{
		_sharePluginManager = new PluginManager();
	}
	return _sharePluginManager;
}

void PluginManager::addPlugin(MatrixPlugin* plugin)
{
	if(plugin)
	{
		pluginCache[plugin->getPluginName()] = plugin;
	}
}

MatrixPlugin* PluginManager::getPlugin(std::string pluginName)
{
	return pluginCache[pluginName];
}

void PluginManager::setupPlugins(bool debug)
{
	std::map<std::string ,MatrixPlugin*>::iterator it;
	for(it=pluginCache.begin();it!=pluginCache.end();++it)
	{
		(it->second)->setupPlugin(debug);
	}
}