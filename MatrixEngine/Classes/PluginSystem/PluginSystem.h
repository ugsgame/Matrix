
#ifndef __MATRIX_PLUGINSYSTEM__
#define __MATRIX_PLUGINSYSTEM__

#define  PLUGIN_CREATE  "CreatePlugin"

class MatrixPlugin;

class PluginSystem
{
public:
	PluginSystem();
	~PluginSystem();
public:

	static PluginSystem* sharePluginSystem();
	
	void setupPluginSystem(bool debug);
	MatrixPlugin* getPlugin(const char* pluginName);
protected:
	const char* loadPluginLibrary(std::string libPath);

private:

	static PluginSystem* _sharePluginSystem;

};

#endif