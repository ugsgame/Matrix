
#ifndef __PLUGINMANAGER__
#define __PLUGINMANAGER__

#include <string>
#include <map>

class MatrixPlugin;

class PluginManager
{
public:
	PluginManager();
	~PluginManager();

public:
	static PluginManager* sharePluginManager();

	void addPlugin(MatrixPlugin* plugin);
	MatrixPlugin* getPlugin(std::string pluginName);

	void setupPlugins(bool debug);

protected:

private:

	static PluginManager* _sharePluginManager;

	std::map<std::string ,MatrixPlugin*> pluginCache;
};

#endif